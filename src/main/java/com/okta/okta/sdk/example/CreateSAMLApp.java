/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okta.okta.sdk.example;

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
import com.okta.sdk.resource.user.User;

/**
 *
 * @author venu
 */
public class CreateSAMLApp {

    public static void main(String[] args) {
        Client client = Clients.builder()
                
                .setOrgUrl("https://{oktaorg}")
                .setClientCredentials(new TokenClientCredentials("{apikey}"))
                .build();

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
