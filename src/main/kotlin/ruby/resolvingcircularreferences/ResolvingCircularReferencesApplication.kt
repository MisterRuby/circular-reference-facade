package ruby.resolvingcircularreferences

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ResolvingCircularReferencesApplication

fun main(args: Array<String>) {
    runApplication<ResolvingCircularReferencesApplication>(*args)
}
