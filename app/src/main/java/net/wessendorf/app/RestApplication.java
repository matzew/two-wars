package net.wessendorf.app;

import javax.ws.rs.core.Application;
import javax.ws.rs.ApplicationPath;

/**
 * The JAX-RS {@link Application} representing the base
 * entry point for all RESTful HTTP requests.
 */
@ApplicationPath("/rest")
public class RestApplication extends Application {
}
