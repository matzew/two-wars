/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wessendorf.app;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.models.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.RealmModel;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.services.managers.RealmManager;
import org.keycloak.services.resources.KeycloakApplication;
import org.keycloak.util.JsonSerialization;
import org.keycloak.util.KeycloakUriBuilder;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationProvider implements ServletContextListener {

    static {
        Config.setAdminRealm("UnifiedPushServer");
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        KeycloakSessionFactory factory = KeycloakApplication.createSessionFactory();
        KeycloakSession session = factory.createSession();
        session.getTransaction().begin();
        try {

            RealmManager manager = new RealmManager(session);

            RealmModel realm = manager.getRealm(Config.getAdminRealm());



            ServletContext servletContext= servletContextEvent.getServletContext();

            AdapterDeploymentContext deploymentContext = (AdapterDeploymentContext)servletContext.getAttribute(AdapterDeploymentContext.class.getName());
            AdapterConfig adapterConfig = new AdapterConfig();


            String uri = KeycloakUriBuilder.fromUri("http://localhost:8080").path(servletContext.getContextPath()).build().toString();
            adapterConfig.setRealm("UnifiedPushServer");
            adapterConfig.setResource("my-app");
            adapterConfig.setRealmKey(realm.getPublicKeyPem());
            Map<String, String> creds = new HashMap<String, String>();
            creds.put(CredentialRepresentation.SECRET, "password");
            adapterConfig.setCredentials(creds);
            adapterConfig.setAuthServerUrl(uri);
            adapterConfig.setSslNotRequired(true);
            deploymentContext.updateDeployment(adapterConfig);

            session.getTransaction().commit();
        } finally {
            session.close();
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
