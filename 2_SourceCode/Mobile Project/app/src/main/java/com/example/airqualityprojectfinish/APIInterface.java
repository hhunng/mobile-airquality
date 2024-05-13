package com.example.airqualityprojectfinish;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface APIInterface {
    @FormUrlEncoded
    @POST("auth/realms/master/protocol/openid-connect/token")
    Call<tokenAsset> getAsset(@Field("client_id") String client_id, @Field("grant_type") String grant_type, @Field("password") String password, @Field("username") String username);

    @GET("api/master/asset/{assetID}")
    Call<Object> getAttributes(@Path("assetID") String assetID);

    @POST("api/master/asset/datapoint/{assetId}/attribute/{attributeName}")
    Call<Object> postAttributeData(
            @Path("assetId") String assetId,
            @Path("attributeName") String attributeName,
            @Body AttributeData attributeData
    );

}
