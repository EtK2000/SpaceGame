package com.etk2000.particles;

import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

/** The source of particles. */
public class ParticleEmitter {

    private static Random randomGenerator = new Random();
    private final List<Particle> particles;
    private Vector3f location;
    private float spawningRate;
    private int particleLifeTime;
    private Vector3f gravity;
    private Vector3f initialVelocity;
    private boolean enable3D;
    private float velocityModifier;

    public ParticleEmitter() {
        this(new Vector3f(0, 0, 0), 3, 300, new Vector3f(0, -0.0003f, 0), false, new Vector3f(-0.5f, 0, -0.5f), 1.0f);
    }

    /**
     * @param location the location of the particle emitter
     * @param spawningRate the amount of particles generated every call to 'ParticleEmitter.update()'
     * @param particleLifeTime the life time of the particle in calls to 'ParticleEmitter.update()'
     * @param gravity the gravity acceleration applied to all the particles each call to 'ParticleEmitter.update()'
     * @param enable3D whether 3D particle generation is enabled
     * @param initialVelocity the base initial velocity
     * @param velocityModifier the particle velocity modifier
     */
    public ParticleEmitter(Vector3f location, float spawningRate, int particleLifeTime, Vector3f gravity,
                           boolean enable3D, Vector3f initialVelocity, float velocityModifier) {
        this.location = location;
        this.spawningRate = spawningRate;
        this.particleLifeTime = particleLifeTime;
        this.gravity = gravity;
        this.enable3D = enable3D;
        this.particles = new ArrayList<Particle>((int) spawningRate * particleLifeTime);
        this.initialVelocity = initialVelocity;
        this.velocityModifier = velocityModifier;
    }

    public float getVelocityModifier() {
        return velocityModifier;
    }

    public void setVelocityModifier(float velocityModifier) {
        this.velocityModifier = velocityModifier;
    }

    public Vector3f getLocation() {
        return location;
    }

    public void setLocation(Vector3f location) {
        this.location = location;
    }

    public float getSpawningRate() {
        return spawningRate;
    }

    public void setSpawningRate(float spawningRate) {
        this.spawningRate = spawningRate;
    }

    public Vector3f getGravity() {
        return gravity;
    }

    public void setGravity(Vector3f gravity) {
        this.gravity = gravity;
    }

    public int getParticleLifeTime() {
        return particleLifeTime;
    }

    public void setParticleLifeTime(int particleLifeTime) {
        this.particleLifeTime = particleLifeTime;
    }

    public Vector3f getInitialVelocity() {
        return initialVelocity;
    }

    public void setInitialVelocity(Vector3f initialVelocity) {
        this.initialVelocity = initialVelocity;
    }

    public boolean isEnable3D() {
        return enable3D;
    }

    public void setEnable3D(boolean enable3D) {
        this.enable3D = enable3D;
    }

    private Particle generateNewParticle(int dx, int dy) {
        Vector3f particleLocation = new Vector3f(location);
        Vector3f particleVelocity = new Vector3f();
        float randomX = (float) randomGenerator.nextDouble() - 0.5f;
        float randomY = (float) randomGenerator.nextDouble() - 0.5f;
        float randomZ = 0;
        if (enable3D) {
            randomZ = (float) randomGenerator.nextDouble() - 0.5f;
        }
        particleVelocity.x = (randomX + initialVelocity.x + dx / 10) / 120;
        particleVelocity.y = (randomY + initialVelocity.y + dy / 10) / 120;
        if (enable3D) {
            particleVelocity.z = (randomZ + initialVelocity.z) / 120;
        }
        particleVelocity.scale(velocityModifier);
        return new Particle(particleLocation, particleVelocity, particleLifeTime);
    }

    /** Update the particle emitter. This does not render anything. */
    public void update() {
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            particle.update(gravity);
            if (particle.isDestroyed()) {
                particles.remove(i);
                i--;
            }
        }
        //if (enable3D) {
        //	if (!Mouse.isButtonDown(0)) {
                for (int i = 0; i < spawningRate; i++) {
                    particles.add(generateNewParticle(0, 0));
                }
        //    }
        /*} else {
            float mouseX = ((float) Mouse.getX() / Display.getWidth() - 0.5f) * 2;
            float mouseY = ((float) Mouse.getY() / Display.getHeight() - 0.5f) * 2;
            if (Mouse.isButtonDown(0)) {
                location.set(mouseX, mouseY);
                int dx = Mouse.getDX();
                int dy = Mouse.getDY();
                for (int i = 0; i < spawningRate; i++) {
                    particles.add(generateNewParticle(dx, dy));
                }
            }
        }*/
    }

    public void draw() {
        glPushAttrib(GL_ALL_ATTRIB_BITS);
        glBegin(GL_POINTS);
        for (Particle particle : particles) {
            float color = (float) particle.expireTime / particleLifeTime;
            glColor4f(color, 1, 1, color);
            //glColor4f(color, 0.2f * color, 0.2f * color, color);
            glVertex3f(particle.position.x, particle.position.y, particle.position.z);
        }
        glEnd();
        glPopAttrib();
    }

    private static class Particle {

        public Vector3f position;
        public Vector3f velocity;
        public int expireTime;

        private Particle(Vector3f position, Vector3f velocity, int expireTime) {
            this.position = position;
            this.velocity = velocity;
            this.expireTime = expireTime;
        }

        public boolean isDestroyed() {
            return expireTime <= 0;
        }

        public void update(Vector3f gravity) {
            position.translate(velocity.x, velocity.y, velocity.z);
            velocity.translate(gravity.x, gravity.y, gravity.z);
            expireTime -= 1;
        }

        @Override
        public String toString() {
            return "Particle{" +
                    "position=" + position +
                    ", velocity=" + velocity +
                    ", expireTime=" + expireTime +
                    '}';
        }
    }
}
