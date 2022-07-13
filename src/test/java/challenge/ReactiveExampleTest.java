package challenge;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;


@ExtendWith(MockitoExtension.class)
class ReactiveExampleTest {

    @InjectMocks
    ReactiveExample reactiveExample;


    @Test
    void sumaDePuntajes() {
        Mono<Integer> suma = reactiveExample.sumaDePuntajes();
        StepVerifier.create(suma)
                .expectNext(260)
                .verifyComplete();
    }

    @Test
    void mayorPuntajeDeEstudiante() {
        Flux<Estudiante> flux = reactiveExample.mayorPuntajeDeEstudiante(2);
        StepVerifier.create(flux)
                .expectNextMatches(estudiante -> estudiante.getNombre().equals("pedro"))
                .expectNextMatches(estudiante -> estudiante.getNombre().equals("juan"))
                .verifyComplete();
    }

    @Test
    void totalDeAsisntenciasDeEstudiantesComMayorPuntajeDe() {
        Mono<Integer> suma = reactiveExample.totalDeAsisntenciasDeEstudiantesConMayorPuntajeDe(75);
        StepVerifier.create(suma)
                .expectNext(43)
                .verifyComplete();
    }

    @Test
    void elEstudianteTieneAsistenciasCorrectas() {
        Mono<Boolean> tieneAsistencias = reactiveExample.elEstudianteTieneAsistenciasCorrectas(
                new Estudiante("raul", 30, List.of(5, 2, 1, 4, 5))
        );
        StepVerifier.create(tieneAsistencias)
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void elEstudianteNoTieneAsistenciasCorrectas() {
        Mono<Boolean> tieneAsistencias = reactiveExample.elEstudianteTieneAsistenciasCorrectas(
                new Estudiante("raul", 30, List.of(1, 2, 1, 4, 3))
        );
        StepVerifier.create(tieneAsistencias)
                .expectNext(Boolean.FALSE)
                .verifyComplete();
    }

    @Test
    void promedioDePuntajesPorEstudiantes() {
        Mono<Double> promedio = reactiveExample.promedioDePuntajesPorEstudiantes();
        StepVerifier.create(promedio)
                .expectNext(52.0)
                .verifyComplete();
    }

    @Test
    void nombreEstudiantesConPuntajeMayorA() {
        Flux<String> nombres = reactiveExample.losNombresDeEstudianteConPuntajeMayorA(74);
        StepVerifier.create(nombres)
                .expectNext("juan", "pedro")
                .verifyComplete();
    }

    @Test
    void estudiantesAprovados() {
        Flux<String> nombres = reactiveExample.estudiantesAprovados();
        StepVerifier.create(nombres)
                .expectNext("juan", "pedro")
                .verifyComplete();
    }
}