package org.extensions.proxy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import net.lightbody.bmp.core.har.HarPage;
import net.lightbody.bmp.mitm.TrustSource;
import net.lightbody.bmp.mitm.util.TrustUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.Proxy;
import java.io.File;
import java.net.Inet4Address;
import java.text.DateFormat;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MobProxyExtension {
    private final Proxy proxy;
    private final BrowserMobProxyServer server;
    public synchronized Proxy getProxy() { return this.proxy; }
    public synchronized BrowserMobProxyServer getServer() { return this.server; }
    public synchronized Har getHar() { return this.server.getHar(); }

    public MobProxyExtension() {
        try {

            BrowserMobProxyServer mobProxyServer = new BrowserMobProxyServer();
            mobProxyServer.setTrustAllServers(true);
            mobProxyServer.setMitmDisabled(true);
            mobProxyServer.setConnectTimeout(30, TimeUnit.SECONDS);

            EnumSet<CaptureType> captureTypes = CaptureType.getAllContentCaptureTypes();
            captureTypes.addAll(CaptureType.getCookieCaptureTypes());
            captureTypes.addAll(CaptureType.getHeaderCaptureTypes());
            captureTypes.addAll(CaptureType.getRequestCaptureTypes());
            captureTypes.addAll(CaptureType.getResponseCaptureTypes());
            mobProxyServer.enableHarCaptureTypes(captureTypes);

            TrustSource trustSource = TrustSource.defaultTrustSource();
            trustSource.add(TrustUtil.getBuiltinTrustedCAs());
            trustSource.add(TrustUtil.getJavaTrustedCAs());
            trustSource.add(TrustUtil.getDefaultJavaTrustManager().getAcceptedIssuers());
            mobProxyServer.setTrustSource(trustSource);

            mobProxyServer.start(0, Inet4Address.getLocalHost());

            mobProxyServer.addResponseFilter((httpResponse, httpMessageContents, httpMessageInfo) -> {
                log.debug("http request " + httpMessageInfo.getOriginalRequest().toString());
                log.debug("http response " + httpResponse.toString());
            });

            String hostIp = Inet4Address.getLocalHost().getHostAddress();
            this.proxy = ClientUtil.createSeleniumProxy(mobProxyServer)
                    .setHttpProxy(hostIp + ":" + mobProxyServer.getPort())
                    .setSslProxy(hostIp + ":" + mobProxyServer.getPort());

            this.server = mobProxyServer;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public synchronized void createNewHar() {
        try {
            this.server.newHar();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public synchronized void stop() {
        try {
            if (this.server != null && !this.server.isStopped()) {
                this.server.stop();
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public synchronized void writeHarFile(
            File harFile, HarLog log, DateFormat dateFormat,
            List<HarEntry> overrideEntries, List<HarPage> overrideHarPage) {

        try {

            JsonGenerator jsonGenerator = new JsonFactory().createGenerator(harFile, JsonEncoding.UTF8);
            String version = log.getVersion();
            jsonGenerator.setCodec(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL));

            jsonGenerator.useDefaultPrettyPrinter();
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("log");
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("version");
            jsonGenerator.writeObject(version);
            jsonGenerator.writeFieldName("creator");
            jsonGenerator.writeObject(log.getCreator());
            jsonGenerator.writeFieldName("pages");
            jsonGenerator.writeStartArray();

            List<HarPage> getHarPages = overrideHarPage != null && overrideHarPage.size() > 0 ? overrideHarPage : log.getPages();

            for (HarPage page : getHarPages) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeFieldName("startedDateTime");
                jsonGenerator.writeObject(dateFormat.format(page.getStartedDateTime()));
                jsonGenerator.writeFieldName("id");
                jsonGenerator.writeObject(page.getId());
                jsonGenerator.writeFieldName("title");
                jsonGenerator.writeObject(page.getTitle());
                jsonGenerator.writeFieldName("pageTimings");
                jsonGenerator.writeObject(page.getPageTimings());
                jsonGenerator.writeEndObject();
            }

            jsonGenerator.writeEndArray();
            jsonGenerator.writeFieldName("entries");
            jsonGenerator.writeStartArray();

            List<HarEntry> getHarEntries = overrideEntries != null && overrideEntries.size() > 0 ? overrideEntries : log.getEntries();

            for (HarEntry entry : getHarEntries) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeFieldName("startedDateTime");
                jsonGenerator.writeObject(dateFormat.format(entry.getStartedDateTime()));
                jsonGenerator.writeFieldName("time");
                jsonGenerator.writeObject(entry.getTime());
                jsonGenerator.writeFieldName("request");
                jsonGenerator.writeObject(entry.getRequest());
                jsonGenerator.writeFieldName("response");
                jsonGenerator.writeObject(entry.getResponse());
                jsonGenerator.writeFieldName("timings");
                jsonGenerator.writeObject(entry.getTimings());
                jsonGenerator.writeFieldName("serverIPAddress");
                jsonGenerator.writeObject(entry.getServerIPAddress());
                jsonGenerator.writeFieldName("connection");
                jsonGenerator.writeObject(entry.getConnection());
                jsonGenerator.writeFieldName("pageref");
                jsonGenerator.writeObject(entry.getPageref());
                jsonGenerator.writeEndObject();
            }

            jsonGenerator.writeEndArray();
            jsonGenerator.writeEndObject();
            jsonGenerator.close();

        } catch (Exception exception) {
            throw new RuntimeException("har json generator error " + exception.getMessage(), exception);
        }
    }
}
