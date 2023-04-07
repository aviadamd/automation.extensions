package org.extensions.automation.proxy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import net.lightbody.bmp.core.har.HarPage;
import net.lightbody.bmp.mitm.TrustSource;
import net.lightbody.bmp.mitm.util.TrustUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Proxy;
import org.springframework.util.SocketUtils;
import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.EnumSet;

public class MobProxyExtension {
    private Proxy proxy;
    private final BrowserMobProxyServer server;
    public Proxy getProxy() { return this.proxy; }
    public BrowserMobProxyServer getServer() { return this.server; }

    public MobProxyExtension(ProxyType proxyType, InetAddress inetAddress) {
        this.server = this.setServer(inetAddress, proxyType);
    }

    private BrowserMobProxyServer setServer(InetAddress inetAddress, ProxyType proxyType) {
        try {

            BrowserMobProxyServer mobProxyServer = new BrowserMobProxyServer();
            mobProxyServer.setTrustAllServers(true);
            mobProxyServer.setMitmDisabled(true);

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

            int port = SocketUtils.findAvailableTcpPort(SocketUtils.PORT_RANGE_MIN, SocketUtils.PORT_RANGE_MAX);
            mobProxyServer.start(port, inetAddress);

            if (proxyType == ProxyType.WEB) this.proxy = this.setSeleniumProxy(mobProxyServer);
            return mobProxyServer;

        } catch (Exception exception) {
            Assertions.fail("init mob proxy fails " + exception.getMessage(), exception);
            return null;
        }
    }

    private Proxy setSeleniumProxy(BrowserMobProxyServer browserMobProxy) {
        try {
            String hostIp = Inet4Address.getLocalHost().getHostAddress();
            Proxy seleniumProxy = ClientUtil.createSeleniumProxy(browserMobProxy);
            seleniumProxy.setHttpProxy(hostIp + ":" + browserMobProxy.getPort());
            seleniumProxy.setSslProxy(hostIp + ":" + browserMobProxy.getPort());
            return seleniumProxy;
        } catch (Exception exception) {
            Assertions.fail("init selenium proxy fails ", exception);
            return null;
        }
    }

    public void writeHarFile(File harFile, HarLog log) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dddd:MM:ss");
        ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.generatePageFromHarLog(harFile, objectMapper, log, dateFormat);
    }

    private void generatePageFromHarLog(File harFile, ObjectMapper objectMapper, HarLog log, DateFormat dateFormat) {
        try {

            JsonGenerator jsonGenerator = new JsonFactory().createGenerator(harFile, JsonEncoding.UTF8);
            String version = log.getVersion();
            jsonGenerator.setCodec(objectMapper);
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

            for (HarPage page : log.getPages()) {
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

            for (HarEntry entry : log.getEntries()) {
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
            Assertions.fail("har json generator error ", exception);
        }
    }
}
