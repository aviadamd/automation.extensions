package org.mobile.data;

import lombok.Data;

@Data
public class ElementsConstants {

    public static class ClientPackages {
        public static final String IOS = "EP.hapoalim.hapoalim";
        public static final String ANDROID = "com.ideomobile.hapoalim";
    }

    public static class Drivers {
        public static final String ANDROID = "io.appium.java_client.android.AndroidDriver";
        public static final String IOS = "io.appium.java_client.android.IOSDriver";
    }

    public static class Ios {
        public static class ClassType {
            public static final String STATIC_TEXT = "XCUIElementTypeStaticText";
            public static final String PICKER_WHEEL = "XCUIElementTypePickerWheel";
            public static final String PICKER_DATE = "XCUIElementTypeDatePicker";
            public static final String EDIT_TEXT = "XCUIElementTypeTextField";
            public static final String WINDOW = "XCUIElementTypeWindow";
            public static final String TABLE = "XCUIElementTypeTable";
            public static final String CELL = "XCUIElementTypeCell";
            public static final String TYPE_OTHER = "XCUIElementTypeOther";
            public static final String BUTTON = "XCUIElementTypeButton";
        }

        public static class FollowingSiblings {
            public static final String OTHER = "following-sibling::XCUIElementTypeOther";
            public static final String CELL = "following-sibling::XCUIElementTypeCell";
            public static final String TEXT = "following-sibling::XCUIElementTypeStaticText";
            public static final String BUTTON = "following-sibling::XCUIElementTypeButton";
        }

        public static class Following {
            public static final String OTHER = "following::XCUIElementTypeOther";
            public static final String CELL = "following::XCUIElementTypeCell";
            public static final String TEXT = "following::XCUIElementTypeStaticText";
            public static final String SIBLING = "following::XCUIElementTypeButton";
        }

        public static class PrecedingSiblings {
            public static final String TEXT = "preceding-sibling::XCUIElementTypeStaticText";
            public static final String OTHER = "preceding-sibling::XCUIElementTypeOther";
            public static final String CELL = "preceding-sibling::XCUIElementTypeCell";
            public static final String SIBLING = "preceding-sibling::XCUIElementTypeButton";
        }

        public static class Preceding {
            public static final String TEXT = "preceding::XCUIElementTypeStaticText";
            public static final String BUTTON = "preceding::XCUIElementTypeButton";
            public static final String OTHER = "preceding::XCUIElementTypeOther";
            public static final String CELL = "preceding::XCUIElementTypeCell";
        }
    }

    public static class Android {

        public static class Infrastructure {
            public static final String DAY_PICKER_VIEW = "android:id/day_picker_view_pager";
            public static final String MONTH_VIEW = "android:id/month_view";
            public static final String SETTINGS_ID = "com.android.settings:id/";
            public static final String DIALER_ID = "com.google.android.dialer:id/";
        }

        public static class ClassType {
            public static final String RECYCLE_VIEW = "androidx.recyclerview.widget.RecyclerView";
            public static final String LINEAR_LAYOUT_COMPAT = "androidx.appcompat.widget.LinearLayoutCompat";
            public static final String ACTION_BAR = "androidx.appcompat.app.ActionBar$Tab";
            public static final String SWITCH = "android.widget.Switch";
            public static final String BUTTON = "android.widget.Button";
            public static final String SCROLL_VIEW = "android.widget.ScrollView";
            public static final String TEXT_WIDGET = "android.widget.EditText";
            public static final String VIEW_GROUP = "android.view.ViewGroup";
            public static final String FRAME_LAYOUT = "android.widget.FrameLayout";
            public static final String LINEAR_LAYOUT = "android.widget.LinearLayout";
            public static final String TEXT_VIEW = "android.widget.TextView";
            public static final String IMAGE_VIEW = "android.widget.ImageView";
            public static final String VIEW = "android.view.View";
            public static final String TEXT_LAYOUT = "android.text.Layout";
            public static final String RADIO_BUTTON = "android.widget.RadioButton";
            public static final String HORIZONTAL_SCROLL_VIEW = "android.widget.HorizontalScrollView";
            public static final String WIDGET_EXPANDABLE_LIST_VIEW = "android.widget.ExpandableListView";
            public static final String IMAGE_BUTTON = "android.widget.ImageButton";
        }

        public static class Following {
            public static final String BUTTON = "following::android.widget.Button";
            public static final String FRAME = "following::android.widget.FrameLayout";
            public static final String TEXT = "following::android.widget.TextView";
            public static final String LINEAR_LAYOUT = "following::android.widget.LinearLayout";
            public static final String SWITCH = "following::android.widget.Switch";
        }

        public static class PreviousSibling {
            public static final String TEXT_VIEW = "previous-sibling::android.widget.TextView";
            public static final String IMAGE_VIEW = "previous-sibling::android.widget.ImageView";
            public static final String BUTTON = "previous-sibling::android.widget.Button";
            public static final String FRAME = "previous-sibling::android.widget.FrameLayout";
            public static final String TEXT = "previous-sibling::android.widget.TextView";
            public static final String LINEAR_LAYOUT = "previous-sibling::android.widget.LinearLayout";
            public static final String SWITCH = "previous-sibling::android.widget.Switch";
        }

        public static class Preceding {
            public static final String PROGRESS_BAR = "preceding::android.widget.ProgressBar";
            public static final String TEXT = "preceding::android.widget.TextView";
            public static final String TEXT_VIEW = "preceding::android.widget.TextView";
            public static final String IMAGE_VIEW = "preceding::android.widget.ImageView";
            public static final String BUTTON = "preceding::android.widget.Button";
            public static final String FRAME = "preceding::android.widget.FrameLayout";
            public static final String LINEAR_LAYOUT = "preceding::android.widget.LinearLayout";
            public static final String SWITCH = "preceding::android.widget.Switch";
        }

        public static class PrecedingSiblings {
            public static final String BUTTON = "preceding-sibling::android.widget.Button";
            public static final String FRAME_LAYOUT = "preceding-sibling::android.widget.FrameLayout";
            public static final String TEXT = "preceding-sibling::android.widget.TextView";
            public static final String EDIT_TEXT = "preceding-sibling::android.widget.EditText";
            public static final String LINEAR_LAYOUT = "preceding-sibling::android.widget.LinearLayout";
            public static final String VIEW = "preceding-sibling::android.view.View";
            public static final String IMAGE = "preceding-sibling::android.widget.ImageView";
            public static final String SWITCH = "preceding-sibling::android.widget.Switch";
        }

        public static class FollowingSiblings {
            public static final String LAYOUT_COMPAT = "following-sibling::androidx.appcompat.widget.LinearLayoutCompat";
            public static final String ACTION_BUTTON = "following-sibling::androidx.appcompat.app.ActionBar$Tab";
            public static final String RADIO_BUTTON = "following-sibling::android.widget.RadioButton";
            public static final String VIEW_GROUP = "following-sibling::android.view.ViewGroup";
            public static final String BUTTON = "following-sibling::android.widget.Button";
            public static final String FRAME_LAYOUT = "following-sibling::android.widget.FrameLayout";
            public static final String TEXT_VIEW = "following-sibling::android.widget.TextView";
            public static final String IMAGE_VIEW = "following-sibling::android.widget.ImageView";
            public static final String LINEAR_LAYOUT = "following-sibling::android.widget.LinearLayout";
            public static final String VIEW = "following-sibling::android.view.View";
            public static final String SWITCH = "following-sibling::android.widget.Switch";
            public static final String EDIT_TEXT = "following-sibling::android.widget.EditText";
            public static final String VIEW_PAGER = "following-sibling::com.android.internal.widget.ViewPager";
        }

        public static class Scrollable {
            public static final String COMMAND_TO_END_OF_VIEW = "new UiScrollable(new UiSelector().scrollable(true)).flingToEnd(10)";
            public static final String COMMAND_TO_START_OF_VIEW = "new UiScrollable(new UiSelector().scrollable(true)).flingBackward()";
        }
    }
}
