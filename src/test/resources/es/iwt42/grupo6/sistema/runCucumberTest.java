package es.iwt42.grupo6.sistema;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/es/iwt42/grupo6/sistema", // Ruta a .feature
        glue = "es.iwt42.grupo6.sistema",                        // Paquete de Steps
        plugin = {"pretty"}
)
public class runCucumberTest {

}