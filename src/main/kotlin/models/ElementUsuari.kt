package models

import kotlinx.serialization.Serializable

/**
 * Relació entre un usuari i un element del catàleg.
 *
 * @property elementOciId identificador de l'element.
 * @property estado estat actual de l'element per a aquest usuari.
 */
@Serializable

class ElementUsuari(
    val elementOciId: String,
    var estado: Estado
)
{

    /**
     * Avança l'estat a la següent etapa disponible, si existeix.
     */
    fun avançarEstat() {
        val siguienteId = estado.id + 1

        val siguienteEstado = Estado.obtenirEstat(siguienteId)

        if (siguienteEstado != null) {
            estado = siguienteEstado
        }
    }


    /**
     * Retrocedeix l'estat a l'etapa anterior disponible, si existeix.
     */
    fun retrocedirEstat() {

        val anteriorId = estado.id - 1

        val anteriorEstado = Estado.obtenirEstat(anteriorId)

        if (anteriorEstado != null) {
            estado = anteriorEstado
        }
    }

}