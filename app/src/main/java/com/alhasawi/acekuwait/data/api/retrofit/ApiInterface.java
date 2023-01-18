package com.alhasawi.acekuwait.data.api.retrofit;

import com.alhasawi.acekuwait.data.api.model.pojo.SearchProductListResponse;
import com.alhasawi.acekuwait.data.api.response.AddressResponse;
import com.alhasawi.acekuwait.data.api.response.CartResponse;
import com.alhasawi.acekuwait.data.api.response.ChangePasswordResponse;
import com.alhasawi.acekuwait.data.api.response.CheckoutResponse;
import com.alhasawi.acekuwait.data.api.response.DeleteAddressResponse;
import com.alhasawi.acekuwait.data.api.response.DynamicContentResponse;
import com.alhasawi.acekuwait.data.api.response.FilterResponse;
import com.alhasawi.acekuwait.data.api.response.ForgotPasswordResponse;
import com.alhasawi.acekuwait.data.api.response.GetAllAddressResponse;
import com.alhasawi.acekuwait.data.api.response.GetNotifyListResponse;
import com.alhasawi.acekuwait.data.api.response.HomeResponse;
import com.alhasawi.acekuwait.data.api.response.LanguageResponse;
import com.alhasawi.acekuwait.data.api.response.LoginResponse;
import com.alhasawi.acekuwait.data.api.response.MainCategoryResponse;
import com.alhasawi.acekuwait.data.api.response.NotifyResponse;
import com.alhasawi.acekuwait.data.api.response.OrderHistoryResponse;
import com.alhasawi.acekuwait.data.api.response.OrderResponse;
import com.alhasawi.acekuwait.data.api.response.OrderReturnResponse;
import com.alhasawi.acekuwait.data.api.response.OriginCategoryResponse;
import com.alhasawi.acekuwait.data.api.response.PaymentResponse;
import com.alhasawi.acekuwait.data.api.response.ProductDetailsResponse;
import com.alhasawi.acekuwait.data.api.response.ProductResponse;
import com.alhasawi.acekuwait.data.api.response.ReturnPolicyResponse;
import com.alhasawi.acekuwait.data.api.response.ReturnReasonResponse;
import com.alhasawi.acekuwait.data.api.response.ReturnResponse;
import com.alhasawi.acekuwait.data.api.response.SearchResponse;
import com.alhasawi.acekuwait.data.api.response.SearchedProductDetailsResponse;
import com.alhasawi.acekuwait.data.api.response.SignupResponse;
import com.alhasawi.acekuwait.data.api.response.StoreResponse;
import com.alhasawi.acekuwait.data.api.response.UserProfileResponse;
import com.alhasawi.acekuwait.data.api.response.WishlistResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {


    @Headers("Content-Type: application/json")
    @POST("customers/authenticate")
    Call<LoginResponse> login(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("customers")
    Call<SignupResponse> signup(@Body RequestBody requestBody);

    @GET("customers/forgotPassword")
    Call<ForgotPasswordResponse> forgotPassword(@Query("emailId") String emailId);

    @GET("customers/forgotPassword/check")
    Call<ForgotPasswordResponse> verifyEmail(@Query("emailId") String emailId);

    @GET("customers/forgotPassword/change")
    Call<ForgotPasswordResponse> resetPassowrd(@Query("emailId") String emailId, @Query("newPassword") String newPassword, @Query("newConfirm") String confirmPassword);


    @PUT("customers/{customerId}/changePassword?")
    Call<ChangePasswordResponse> changePassword(@Path("customerId") String customerId, @Query("oldPassword") String oldPassword, @Query("newPassword") String newPassword, @Header("Authorization") String sessionToken);

    @GET("languages")
    Call<LanguageResponse> getLanguages();

    @GET("categories/{category_id}")
    Call<OriginCategoryResponse> getCategoriesByOrigin(@Path("category_id") String categoryId, @Query("languageId") String languageId);

    @GET("categories/tree")
    Call<MainCategoryResponse> getMainCategories(@Query("languageId") String language);

    @GET("home")
    Call<HomeResponse> getHomeUiData(@Query("languageId") String languageId);

    @POST("notify")
    Call<NotifyResponse> addToNotifyList(@Body RequestBody requestBody, @Header("Authorization") String sessionToken);

    @GET("notify/{customerId}/{page}/{size}")
    Call<GetNotifyListResponse> getNotifyList(@Path("customerId") String customerId, @Path("page") String page, @Path("size") String size, @Header("Authorization") String sessionToken, @Query("languageId") String languageId);


    @Headers("Content-Type: application/json")
    @POST("products/{page_no}/30?")
    Call<ProductResponse> getProductsList(@Body RequestBody requestBody, @Path("page_no") String page_no, @Query("languageId") String languageId);


    @GET("products/{product_name}")
    Call<ProductDetailsResponse> getProductDetails(@Path("product_name") String product_name);

    @GET("search/0/20?")
    Call<SearchProductListResponse> searchProducts(@Query("q") String query, @Query("languageId") String languageId);

    @GET("search/0/20?")
    Call<SearchResponse> getSearchResults(@Query("q") String query, @Query("category") String category, @Query("languageId") String languageId);

//    @GET("products/search/{product_object_id}")
//    Call<SearchedProductDetailsResponse> getSearchedProductDetails(@Path("product_object_id") String selectedProductObjectId, @Query("languageId") String languageId);

    @GET("products/sku/{sku}")
    Call<SearchedProductDetailsResponse> getSearchedProductDetails(@Path("sku") String sku, @Query("languageId") String languageId, @Query("customerId") String customerId);


    @Headers("Content-Type: application/json")
    @POST("products/filtes")
    Call<FilterResponse> getFilters(@Body RequestBody requestBody, @Query("languageId") String languageId);

    @POST("wishlists/customers/{user_id}/products/{product_id}")
    Call<WishlistResponse> addToWishlist(@Path("user_id") String userID, @Path("product_id") String productID, @Header("Authorization") String sessionToken);

    @GET("wishlists/customers/{user_id}")
    Call<WishlistResponse> getWishlistedProducts(@Path("user_id") String userID, @Header("Authorization") String sessionToken, @Query("languageId") String languageId);

    @Headers("Content-Type: application/json")
    @POST("cart/customers/{customer_id}")
    Call<CartResponse> addToCart(@Path("customer_id") String userId, @Body RequestBody requestBody, @Header("Authorization") String sessionToken, @Query("languageId") String languageId);

    @Headers("Content-Type: application/json")
    @POST("cart/customers/update/{customer_id}")
    Call<CartResponse> updateCartItems(@Path("customer_id") String userId, @Body RequestBody requestBody, @Header("Authorization") String sessionToken, @Query("languageId") String languageId);

    @GET("cart/customers/{customer_id}/cartItem/{cart_item_id}")
    Call<CartResponse> removeFromCart(@Path("customer_id") String userId, @Path("cart_item_id") String cartItemId, @Header("Authorization") String sessionToken, @Query("languageId") String languageId);

    @GET("cart/customers/{customer_id}")
    Call<CartResponse> getCartItems(@Path("customer_id") String userId, @Header("Authorization") String sessionToken, @Query("languageId") String languageId, @Query("alertMsg") boolean isAlertClicked);

    @Headers("Content-Type: application/json")
    @POST("customers/{customer_id}/address")
    Call<AddressResponse> addNewAddress(@Path("customer_id") String userId, @Body RequestBody requestBody, @Header("Authorization") String sessionToken, @Query("languageId") String languageId);

    @GET("order/customers/{customerId}/cart/{cartId}/?")
    Call<CheckoutResponse> checkout(@Path("customerId") String customerID, @Path("cartId") String cartId, @Query("couponCode") String couponCode, @Header("Authorization") String sessionToken, @Query("languageId") String languageId);

    @Headers("Content-Type: application/json")
    @POST("order/customers/{customerId}/cart/{cartId}")
    Call<OrderResponse> orderConfirmation(@Path("customerId") String customerID, @Path("cartId") String cartId, @Header("Authorization") String sessionToken, @Body RequestBody requestBody, @Query("languageId") String languageId);

    @GET("customers/{customerId}/address")
    Call<GetAllAddressResponse> getAddresses(@Path("customerId") String customerId, @Header("Authorization") String sessionToken, @Query("languageId") String languageId);

    @DELETE("address/{addressId}")
    Call<DeleteAddressResponse> deleteAddress(@Path("addressId") String addressId, @Header("Authorization") String sessionToken);

    @Headers("Content-Type: application/json")
    @POST("customers/{customerId}/address/{addressId}")
    Call<AddressResponse> editAddress(@Path("customerId") String customerId, @Path("addressId") String addressId, @Header("Authorization") String sessionToken, @Body RequestBody requestBody);

    @GET("customers/findByEmail?")
    Call<UserProfileResponse> userProfile(@Query("emailId") String emailId, @Header("Authorization") String sessionToken, @Query("languageId") String languageId);

    @Headers("Content-Type: application/json")
    @PUT("customers/{customerId}")
    Call<UserProfileResponse> editUserProfile(@Path("customerId") String customerId, @Header("Authorization") String sessionToken, @Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("order/customers/{customerId}")
    Call<OrderHistoryResponse> orderHistory(@Path("customerId") String customerId, @Header("Authorization") String sessionToken, @Query("languageId") String languageId);

    @GET("consistent?")
    Call<DynamicContentResponse> getDynamicWebviewContent(@Query("name") String query, @Query("languageId") String languageId);

    @GET
    Call<PaymentResponse> getPaymentSuccessResponse(@Url String url);

    @GET("order/customer/{customerId}/returnOrder")
    Call<ReturnResponse> getElibileReturnOrders(@Path("customerId") String customerId, @Header("Authorization") String sessionToken, @Query("languageId") String languageId);

    @Headers("Content-Type: application/json")
    @POST("order/returnOrder")
    Call<OrderReturnResponse> orderReturn(@Body RequestBody requestBody, @Header("Authorization") String sessionToken, @Query("languageId") String languageId);

//    @GET("order/{orderId}/returnOrder")
//    Call<OrderReturnResponse> fullOrderReturn(@Path("orderId") String orderId, @Header("Authorization") String sessionToken);
//
//    @GET("order/{orderId}/returnOrder/{orderProductId}/{qty}")
//    Call<OrderReturnResponse> productReturn(@Path("orderId") String orderId, @Path("orderProductId") String orderProductId, @Path("qty") String qty, @Header("Authorization") String sessionToken);

    @GET("consistent?name=Return policy")
    Call<ReturnPolicyResponse> getReturnPolicy(@Query("languageId") String languageId);

    @GET("consistent/reasons")
    Call<ReturnReasonResponse> getReturnReasons(@Query("languageId") String languageId);

    @GET("stores")
    Call<StoreResponse> getStoresList(@Query("languageId") String languageId, @Header("Authorization") String sessionToken);
}
