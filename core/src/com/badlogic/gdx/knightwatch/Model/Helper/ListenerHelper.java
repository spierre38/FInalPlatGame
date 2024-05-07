
package com.badlogic.gdx.knightwatch.Model.Helper;

import com.badlogic.gdx.knightwatch.Model.Objects.Minion;
import com.badlogic.gdx.knightwatch.Controller.Objects.Player;
import com.badlogic.gdx.knightwatch.Model.Objects.Poles;
import com.badlogic.gdx.knightwatch.Model.Objects.TileChangeable;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 *
 * @author ecloaner
 */
public class ListenerHelper implements ContactListener {

    
    
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        
        // Check for contact between sword hitbox and crate
        if ((fixA.getUserData() == "swordHitbox" && fixB.getUserData() instanceof TileChangeable) ||
            (fixB.getUserData() == "swordHitbox" && fixA.getUserData() instanceof TileChangeable)) {
            Fixture swordHitbox = fixA.getUserData() == "swordHitbox" ? fixA : fixB;
            Fixture crate = swordHitbox == fixA ? fixB : fixA;
            
            if (crate.getUserData() != null && TileChangeable.class.isAssignableFrom(crate.getUserData().getClass())) {
                ((TileChangeable) crate.getUserData()).onHitWithSword();
            }
        }
        
        if ((fixA.getUserData() == "handSensor" && fixB.getUserData() instanceof Poles) ||
            (fixB.getUserData() == "handSensor" && fixA.getUserData() instanceof Poles)) {
            Player player = Player.getInstance(null); 
            if (player != null) {
                player.setState(Player.State.HANGING);
            }
        }  
        
        if ((fixA.getUserData() instanceof Player && fixB.getUserData() instanceof Minion) ||
            (fixB.getUserData() instanceof Player && fixA.getUserData() instanceof Minion)) {
            // Call the hit method when the player is hit by a minion
            Player player = fixA.getUserData() instanceof Player ? (Player) fixA.getUserData() : (Player) fixB.getUserData();
            Minion minion = fixA.getUserData() instanceof Minion ? (Minion) fixA.getUserData() : (Minion) fixB.getUserData();
            player.hitByMinion(minion); 
        }
        
        // Check for contact between sword hitbox and enemy
        if ((fixA.getUserData() == "swordHitbox" && fixB.getUserData() instanceof Minion) ||
            (fixB.getUserData() == "swordHitbox" && fixA.getUserData() instanceof Minion)) {
            Fixture swordHitbox = fixA.getUserData() == "swordHitbox" ? fixA : fixB;
            Fixture enemy = swordHitbox == fixA ? fixB : fixA;

            if (enemy.getUserData() != null && enemy.getUserData() instanceof Minion) {
                ((Minion) enemy.getUserData()).onhitWithSword();
            }
        }        
        // Check for contact between hand sensor and objects
        if (fixA.getUserData() == "handSensor" || fixB.getUserData() == "handSensor") {
            Fixture hand = fixA.getUserData() == "handSensor" ? fixA : fixB;
            Fixture object = hand == fixA ? fixB : fixA;
           
            if (object.getUserData() != null && TileChangeable.class.isAssignableFrom(object.getUserData().getClass())) {
                ((TileChangeable) object.getUserData()).handHit();
                
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if ((fixA.getUserData() == "handSensor" && fixB.getUserData() instanceof Poles) ||
            (fixB.getUserData() == "handSensor" && fixA.getUserData() instanceof Poles)) {
            Player player = Player.getInstance(null); 
            if (player != null) {
                player.setState(Player.State.STANDING);
            }
        }

    }
    @Override
    public void preSolve(Contact contact, Manifold mnfld) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
    
}
