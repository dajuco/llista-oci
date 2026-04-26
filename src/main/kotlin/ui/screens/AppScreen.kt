package ui.screens

import androidx.compose.runtime.*
import models.*
import ui.viewmodel.*

@Composable
fun PantallaAplicacio(viewModel: ViewModel) {
    val ociState = viewModel.ociState
    val usuarioLogueado = ociState.usuarioLogueado

    LaunchedEffect(usuarioLogueado?.username) {
        if (usuarioLogueado != null) {
            viewModel.refrescarDadesRolActual()
        }
    }

    if (usuarioLogueado == null) {
        PantallaIniciSessio(
            ociState = ociState,
            onUsuariCanvi = viewModel::actualitzarUsuari,
            onContrasenyaCanvi = viewModel::actualitzarContrasenya,
            onIniciarSessio = viewModel::iniciarSessio
        )
    } else {
        when (usuarioLogueado) {
            is UserSuperAdmin -> PantallaSuperAdmin(
                ociState = ociState,
                onNouNomUsuariCanvi = viewModel::actualitzarNouNomUsuari,
                onNovaContrasenyaCanvi = viewModel::actualitzarNovaContrasenya,
                onNouDisplayCanvi = viewModel::actualitzarNouDisplay,
                onNouEsAdminCanvi = viewModel::actualitzarNouEsAdmin,
                onCrearUsuario = viewModel::crearUsuari,
                onRefrescarUsuarios = viewModel::carregarUsuaris,
                onNetejarMissatges = viewModel::netejarMissatges,
                onLogout = viewModel::tancarSessio
            )

            is UserAdmin -> PantallaAdmin(
                ociState = ociState,
                onNomNovaCategoriaCanvi = viewModel::actualitzarNomNovaCategoria,
                onCrearCategoria = viewModel::crearCategoria,
                onNouElementIdCanvi = viewModel::actualitzarNouElementId,
                onNouElementTitolCanvi = viewModel::actualitzarNouElementTitol,
                onNouElementDescripcioCanvi = viewModel::actualitzarNouElementDescripcio,
                onNouElementCategoriaIdCanvi = viewModel::actualitzarNouElementCategoriaId,
                onCrearElemento = viewModel::crearElemento,
                onRefrescarCatalogo = viewModel::carregarCataleg,
                onNetejarMissatges = viewModel::netejarMissatges,
                onLogout = viewModel::tancarSessio
            )

            is UserNormal -> PantallaIniciUsuari(
                ociState = ociState,
                onElementObjectiuIdCanvi = viewModel::actualitzarElementObjectiuId,
                onAfegirElement = viewModel::afegirElementAUsuari,
                onAvancarEstat = viewModel::avançarEstatElement,
                onRetrocedirEstat = viewModel::retrocedirEstatElement,
                onRefrescar = viewModel::refrescarDadesRolActual,
                onNetejarMissatges = viewModel::netejarMissatges,
                onLogout = viewModel::tancarSessio
            )
        }
    }
}

