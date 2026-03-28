package models

class ElementUsuari(
    val elementOciId: String,
    var estado: Estado
)
{

    fun AvanzarEstado() {
        val siguienteId = estado.id + 1

        val siguienteEstado = Estado.getEstado(siguienteId)

        if (siguienteEstado != null) {
            estado = siguienteEstado
        }
    }


    fun RetrocederEstado() {

        val anteriorId = estado.id - 1

        val anteriorEstado = Estado.getEstado(anteriorId)

        if (anteriorEstado != null) {
            estado = anteriorEstado
        }
    }

}