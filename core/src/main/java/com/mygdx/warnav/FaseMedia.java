package com.mygdx.warnav;

public class FaseMedia extends FaseJuego {
    
    @Override
    protected void configurarFondo(GameScreen screen) {
        screen.setFondoColor(0.05f, 0.1f, 0.2f); 
        screen.setVelocidadEstrellas(5f, 5f, 5f); 
    }

    @Override
    protected void configurarEnemigos(Lluvia lluvia) {
        lluvia.setProbEnemigo(0.8f);
        lluvia.setIntervaloSpawn(0.25f);
    }

    public void aplicarCambioFabrica(Lluvia lluvia, GameScreen screen) {
        FabricaLluvia fabricaNivel2 = new FabricaLluvia.Nivel2(
            screen.getEnemigoTexture(),
            screen.getSoldadoTexture(),
            screen.getPowerUpTexture(),
            screen.getVidaTexture()
        );
        
        lluvia.setFabrica(fabricaNivel2);
    }
}