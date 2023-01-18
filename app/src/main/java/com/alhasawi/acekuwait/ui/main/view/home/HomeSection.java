package com.alhasawi.acekuwait.ui.main.view.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class HomeSection {


    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("align")
    @Expose
    private String align;
    @SerializedName("fontSize")
    @Expose
    private Integer fontSize;
    @SerializedName("dimension")
    @Expose
    private Dimensions dimension;
    @SerializedName("view")
    @Expose
    private View view;
    @SerializedName("elements")
    @Expose
    private List<Element> elements = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Dimensions getDimension() {
        return dimension;
    }

    public void setDimension(Dimensions dimension) {
        this.dimension = dimension;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

}