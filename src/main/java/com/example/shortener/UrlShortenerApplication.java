package com.example.shortener;

import com.example.shortener.auth.ExampleAuthenticator;
import com.example.shortener.auth.ExampleAuthorizer;
import com.example.shortener.core.Person;
import com.example.shortener.core.Url;
import com.example.shortener.core.User;
import com.example.shortener.db.PersonDAO;
import com.example.shortener.db.UrlDAO;
import com.example.shortener.resources.PeopleResource;
import com.example.shortener.resources.ProtectedResource;
import com.example.shortener.resources.UrlShortenerResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class UrlShortenerApplication extends Application<UrlShortenerConfiguration> {
    public static void main(String[] args) throws Exception {
        new UrlShortenerApplication().run(args);
    }

    private final HibernateBundle<UrlShortenerConfiguration> hibernateBundle =
            new HibernateBundle<UrlShortenerConfiguration>(Person.class, Url.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(UrlShortenerConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "url-shortener";
    }

    @Override
    public void initialize(Bootstrap<UrlShortenerConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MigrationsBundle<UrlShortenerConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(UrlShortenerConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(UrlShortenerConfiguration configuration, Environment environment) {
        final PersonDAO dao = new PersonDAO(hibernateBundle.getSessionFactory());
        final UrlDAO urlDAO = new UrlDAO(hibernateBundle.getSessionFactory());

        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new ExampleAuthenticator())
                .setAuthorizer(new ExampleAuthorizer())
                .setRealm("SUPER SECRET STUFF")
                .buildAuthFilter()));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new UrlShortenerResource(urlDAO));
//        environment.jersey().register(new ProtectedResource());
//        environment.jersey().register(new PeopleResource(dao));
    }
}
