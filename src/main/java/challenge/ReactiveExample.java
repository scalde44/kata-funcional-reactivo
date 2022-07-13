package challenge;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class ReactiveExample {

    public static final int VALOR_PERMITIDO = 15;
    public static final int NOTA_MINIMA_EXITOSA = 75;
    private Flux<Estudiante> estudianteFlux;

    public ReactiveExample() {
        this.estudianteFlux = Flux.fromIterable(List.of(
                new Estudiante("raul", 30, List.of(1, 2, 1, 4, 5)),
                new Estudiante("andres", 35, List.of(4, 2, 4, 3, 5)),
                new Estudiante("juan", 75, List.of(3, 2, 4, 5, 5)),
                new Estudiante("pedro", 80, List.of(5, 5, 4, 5, 5)),
                new Estudiante("santiago", 40, List.of(4, 5, 4, 5, 5))
        ));

    }

    public Mono<Integer> sumaDePuntajes() {
        return this.estudianteFlux
                .collect(Collectors.summingInt(Estudiante::getPuntaje));
    }

    public Flux<Estudiante> mayorPuntajeDeEstudiante(int limit) {
        return this.estudianteFlux
                .sort(Comparator.comparing(Estudiante::getPuntaje).reversed())
                .take(limit);

    }

    public Mono<Integer> totalDeAsisntenciasDeEstudiantesConMayorPuntajeDe(int valor) {
        return this.estudianteFlux
                .filter(estudiante -> estudiante.getPuntaje() >= valor)
                .flatMap(estudiante -> Flux.fromIterable(estudiante.getAsistencias()))
                .reduce(Integer::sum);
    }

    public Mono<Boolean> elEstudianteTieneAsistenciasCorrectas(Estudiante estudiante) {
        return Mono.just(estudiante)
                .filter(this.asistenciasPemitidas())
                .hasElement();
    }

    public Mono<Double> promedioDePuntajesPorEstudiantes() {
        return sumaDePuntajes()
                .zipWith(estudianteFlux.count())
                .map(objects -> objects.getT1().doubleValue() / objects.getT2().doubleValue());
    }

    public Flux<String> losNombresDeEstudianteConPuntajeMayorA(int valor) {
        return this.estudianteFlux
                .filter(estudiante -> estudiante.getPuntaje() > valor)
                .map(Estudiante::getNombre);
    }

    public Flux<String> estudiantesAprovados() {
        return this.estudianteFlux
                .flatMap(this::aprobar)
                .filter(Estudiante::isAprobado)
                .map(Estudiante::getNombre);
    }

    private Mono<Estudiante> aprobar(Estudiante estudiante) {
        return Mono.just(estudiante)
                .filter(e -> e.getPuntaje() >= NOTA_MINIMA_EXITOSA)
                .map(e -> {
                    e.setAprobado(true);
                    return e;
                })
                .defaultIfEmpty(estudiante);
    }

    private Predicate<Estudiante> asistenciasPemitidas() {
        return estudiante -> Flux.fromIterable(estudiante.getAsistencias())
                .reduce(Integer::sum)
                .block() >= VALOR_PERMITIDO;
    }
}
