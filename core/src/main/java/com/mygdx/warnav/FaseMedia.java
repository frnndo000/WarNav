package com.mygdx.warnav;

public class FaseMedia extends FaseJuego {
    
    @Override
    protected void configurarFondo(GameScreen screen) {
        screen.setFondoColor(0.05f, 0.1f, 0.2f); 
        screen.setVelocidadEstrellas(5f, 5f, 5f); 
    }

    @Override
    protected void configurarEnemigos(Lluvia lluvia) {
        // Ajustamos dificultad para que se vean los premios
        lluvia.setProbEnemigo(0.7f);
        lluvia.setIntervaloSpawn(0.20f);
    }

    // Este método se llama desde GestorFases cuando entras en la fase media
    public void aplicarCambioFabrica(Lluvia lluvia, GameScreen screen) {
        //Creamos la fábrica con todas las texturas necesarias
        FabricaLluvia fabricaNivel2 = new FabricaLluvia.Nivel2(
            screen.getEnemigoTexture(),
            screen.getSoldadoTexture(),
            screen.getPowerUpTexture(),
            screen.getVidaTexture(),
            screen.getMultiplicadorTexture() 
        );
        
        lluvia.setFabrica(fabricaNivel2);
    }
}