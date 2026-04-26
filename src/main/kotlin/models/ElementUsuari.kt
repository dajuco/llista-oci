package models

import kotlinx.serialization.Serializable

@Serializable

class ElementUsuari(
    val elementOciId: String,
    var estado: Estado
)
{

    fun avançarEstat() {
        val siguienteId = estado.id + 1

        val siguienteEstado = Estado.obtenirEstat(siguienteId)

        if (siguienteEstado != null) {
            estado = siguienteEstado
        }
    }


    fun retrocedirEstat() {

        val anteriorId = estado.id - 1

        val anteriorEstado = Estado.obtenirEstat(anteriorId)

        if (anteriorEstado != null) {
            estado = anteriorEstado
        }
    }

}