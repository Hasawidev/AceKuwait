package com.alhasawi.acekuwait.data.api.response;

import com.alhasawi.acekuwait.data.api.model.pojo.ProductSearch;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class SearchResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public class Data {
        @SerializedName("page")
        @Expose
        private int page;
        @SerializedName("nbHits")
        @Expose
        private int nbHits;
        @SerializedName("nbPages")
        @Expose
        private int nbPages;
        @SerializedName("hitsPerPage")
        @Expose
        private int hitsPerPage;
        @SerializedName("productSearchs")
        @Expose
        private List<ProductSearch> productSearchList = null;
        @SerializedName("fectes")
        @Expose
        private fectes fectes;
        @SerializedName("categories")
        @Expose
        private HashMap<String, String> categoriesMap;
        @SerializedName("productByCategory")
        @Expose
        private HashMap<String, List<String>> productByCategoryMap;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getNbHits() {
            return nbHits;
        }

        public void setNbHits(int nbHits) {
            this.nbHits = nbHits;
        }

        public int getNbPages() {
            return nbPages;
        }

        public void setNbPages(int nbPages) {
            this.nbPages = nbPages;
        }

        public int getHitsPerPage() {
            return hitsPerPage;
        }

        public void setHitsPerPage(int hitsPerPage) {
            this.hitsPerPage = hitsPerPage;
        }

        public List<ProductSearch> getProductSearchList() {
            return productSearchList;
        }

        public void setProductSearchList(List<ProductSearch> productSearchList) {
            this.productSearchList = productSearchList;
        }

        public Data.fectes getFectes() {
            return fectes;
        }

        public void setFectes(Data.fectes fectes) {
            this.fectes = fectes;
        }

        public HashMap<String, String> getCategoriesMap() {
            return categoriesMap;
        }

        public void setCategoriesMap(HashMap<String, String> categoriesMap) {
            this.categoriesMap = categoriesMap;
        }

        public HashMap<String, List<String>> getProductByCategoryMap() {
            return productByCategoryMap;
        }

        public void setProductByCategoryMap(HashMap<String, List<String>> productByCategoryMap) {
            this.productByCategoryMap = productByCategoryMap;
        }

        class fectes {
            @SerializedName("brand")
            @Expose
            private HashMap<String, Integer> brandsHashMap;
            @SerializedName("active")
            @Expose
            private HashMap<String, Integer> activeHashMap;
            @SerializedName("categories")
            @Expose
            private HashMap<String, Integer> categoriesHashMap;

            public HashMap<String, Integer> getBrandsHashMap() {
                return brandsHashMap;
            }

            public void setBrandsHashMap(HashMap<String, Integer> brandsHashMap) {
                this.brandsHashMap = brandsHashMap;
            }

            public HashMap<String, Integer> getActiveHashMap() {
                return activeHashMap;
            }

            public void setActiveHashMap(HashMap<String, Integer> activeHashMap) {
                this.activeHashMap = activeHashMap;
            }

            public HashMap<String, Integer> getCategoriesHashMap() {
                return categoriesHashMap;
            }

            public void setCategoriesHashMap(HashMap<String, Integer> categoriesHashMap) {
                this.categoriesHashMap = categoriesHashMap;
            }
        }

    }

}
