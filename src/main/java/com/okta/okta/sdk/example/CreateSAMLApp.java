/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okta.okta.sdk.example;

import com.okta.authn.sdk.client.AuthenticationClient;
import com.okta.authn.sdk.client.AuthenticationClients;
import com.okta.authn.sdk.http.Header;
import com.okta.authn.sdk.http.QueryParameter;
import com.okta.authn.sdk.http.RequestContext;
import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.authn.sdk.resource.UnlockAccountRequest;
import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.Client;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.ResourceException;
import com.okta.sdk.resource.application.Application;
import com.okta.sdk.resource.application.ApplicationVisibility;
import com.okta.sdk.resource.application.SamlApplication;
import com.okta.sdk.resource.application.SamlApplicationSettings;
import com.okta.sdk.resource.application.SamlApplicationSettingsSignOn;
import com.okta.sdk.resource.application.SwaApplication;
import com.okta.sdk.resource.application.SwaApplicationSettings;
import com.okta.sdk.resource.application.SwaApplicationSettingsApplication;
import com.okta.sdk.resource.user.ForgotPasswordResponse;
import com.okta.sdk.resource.user.ResetPasswordToken;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserActivationToken;
import com.okta.sdk.resource.user.UserBuilder;
import com.okta.sdk.resource.user.factor.FactorType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author venu
 */
public class CreateSAMLApp {

    public static void main(String[] args) {
        String oktaOrgURL = "{oktaURL}";
        String apiKey = "{apikey}";
        
        Client client = Clients.builder()
                
                .setOrgUrl(oktaOrgURL)
                .setClientCredentials(new TokenClientCredentials(apiKey))
                .build();
        List<Header> headers = new ArrayList<>();
        headers.add(new Header("Authorization", "SSWS "+apiKey));
        List<QueryParameter> queryParameters = new ArrayList<>();
        AuthenticationClient authClient = AuthenticationClients.builder()
            .setOrgUrl(oktaOrgURL)
                
               
            .build();
         RequestContext requestContext = new RequestContext(headers, queryParameters);
         
        /*
         User Activate, Password reset, unlock recovery without triggering email
         */ 
        //userActivateTest(client);
        //resetPasswordSample(client);
        //userUnlockTest(authClient, requestContext);
    }
    
    
    /*
    curl --location --request POST 'https://okta url/api/v1/authn/recovery/unlock' \
    --header 'Accept: application/json' \
    --header 'Content-Type: application/json' \
    --header 'Authorization: SSWS {apikey}' \
    --data-raw '{
      "username": "{oktausername}",
      "relayState": "teststate"
    }  '
    */
      private static void userUnlockTest(AuthenticationClient client, RequestContext request){
        try{
           // AuthenticationResponse response = client.unlockAccount("vsripada@setsolutions.com", null, "relayState", null);
            UnlockAccountRequest unlockRequest = client.instantiate(UnlockAccountRequest.class);
            unlockRequest.setRelayState("relayState");
           
            unlockRequest.setUsername("vsripada@setsolutions.com");
            AuthenticationResponse response = client.unlockAccount(unlockRequest, request, null);
            System.out.println("getting token");
             
            System.out.println(response.toString());
           
            
        }
        catch(Exception e){
            System.out.println(e);
        }
        
    }
    
    
    /*
      curl --location --request POST 'https://{oktaurl}/api/v1/users/{userid}/lifecycle/reset_password?sendEmail=false' \
    --header 'Content-Type: application/json' \
    --header 'Accept: application/json' \
    --header 'Authorization: SSWS {apikey}' \
    --data-raw ''
      */
    private static void resetPasswordSample(Client client){
        User user = client.getUser("vsripada@setsolutions.com");
        ResetPasswordToken token =  user.resetPassword(null, false);
        System.out.println("getting token");
        System.out.println(token);
    }
    
    /*
    curl -v -X POST \
    -H "Accept: application/json" \
    -H "Content-Type: application/json" \
    -H "Authorization: SSWS ${api_token}" \
    "https://${yourOktaDomain}/api/v1/users/00ub0oNGTSWTBKOLGLNR/lifecycle/activate?sendEmail=false"
    */
    
    private static void userActivateTest(Client client) {

        String password = "Passw0rd!2@3#";
        String firstName = "John";
        String lastName = "Activate";
        String email = "john-activate@example.com";

        User user = UserBuilder.instance()
                .setEmail(email)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setPassword(password.toCharArray())
                .setActive(false)
                .buildAndCreate(client);
        
        

        UserActivationToken token = user.activate(false);
        System.out.println(token.getActivationToken());



    }
    
    private static void createApplication(Client client){
         //  Application samlApp = client.getApplication("0oajnewtpb28chytr0h7");
        SamlApplication samlApplication = client.instantiate(SamlApplication.class)
                // samlApplication.setSettings();

                .setSettings(client.instantiate(SamlApplicationSettings.class)
                        .setSignOn(client.instantiate(SamlApplicationSettingsSignOn.class)
                                .setDefaultRelayState("")
                                .setSsoAcsUrl("http://testorgone.okta")
                                .setIdpIssuer("http://www.okta.com/${org.externalKey}")
                                .setAudience("asdqwe123")
                                .setRecipient("http://testorgone.okta")
                                .setDestination("http://testorgone.okta")
                                .setSubjectNameIdTemplate("${user.userName}")
                                .setSubjectNameIdFormat("urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified")
                                .setResponseSigned(true)
                                .setAssertionSigned(true)
                                .setSignatureAlgorithm("RSA_SHA256")
                                .setDigestAlgorithm("SHA256")
                                .setHonorForceAuthn(true)
                                .setAuthnContextClassRef("urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport")
                                .setSpIssuer(null)
                                .setRequestCompressed(false)));

        // .setHide(client.instantiate(ApplicationVisibility.class)
        samlApplication.setVisibility(client.instantiate(ApplicationVisibility.class)
                .setAutoSubmitToolbar(Boolean.FALSE));
        samlApplication.setLabel("API Application");

        //Create Application and use ResourceException to view causes         
        try {
            client.createApplication(samlApplication);
        } catch (ResourceException e) {
            System.out.println("error");
            System.out.println(e.getCauses());
        }

        System.out.println(samlApplication);
    }

}
