package com.alhasawi.acekuwait.ui.main.view.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class View {
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("carouselType")
    @Expose
    private String carousel_type;
    @SerializedName("headerButton")
    @Expose
    private HeaderButton headerButton;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCarousel_type() {
        return carousel_type;
    }

    public void setCarousel_type(String carousel_type) {
        this.carousel_type = carousel_type;
    }

    public HeaderButton getHeaderButton() {
        return headerButton;
    }

    public void setHeaderButton(HeaderButton headerButton) {
        this.headerButton = headerButton;
    }

    public class HeaderButton {
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("show_view")
        @Expose
        private boolean show_view;
        @SerializedName("action")
        @Expose
        private Action action;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isShow_view() {
            return show_view;
        }

        public void setShow_view(boolean show_view) {
            this.show_view = show_view;
        }

        public Action getAction() {
            return action;
        }

        public void setAction(Action action) {
            this.action = action;
        }
    }
}
