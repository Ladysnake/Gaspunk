package ladysnake.gaspunk.client.particle;

import static org.junit.Assert.*;

public class ParticleGasSmokeTest {
    @org.junit.Test
    public void onUpdate() throws Exception {
        int particleAge = 0;
        int particleMaxAge = 100;
        float particleAlpha = 0;
        float particleMaxAlpha = 1;
        for (; ; ) {

            if (particleAge++ >= particleMaxAge) {
                particleAlpha *= .9;
                if (particleAlpha <= 0.01) {
                    System.out.println("Watashi wa mou shindeiru");
                    return;
                }
            } else
                particleAlpha = (float) Math.min(particleAge / (particleMaxAge / 2), 1) * particleMaxAlpha;
            System.out.println(particleAge + " " + particleAlpha);
        }
    }

}