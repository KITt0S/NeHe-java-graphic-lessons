import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;

public class Program extends JFrame {

    Program() {

        GLProfile profile = GLProfile.get( GLProfile.GL2 );
        GLCapabilities capabilities = new GLCapabilities( profile );
        GLCanvas canvas = new GLCanvas( capabilities );
        Field field = new Field();
        canvas.addGLEventListener( field );
        canvas.addKeyListener( field );
        canvas.setSize( 400, 400 );
        getContentPane().add( canvas );
        setSize( getContentPane().getPreferredSize() );
        setVisible( true );
        FPSAnimator animator = new FPSAnimator( canvas, 300, true );
        animator.start();
    }

    public static void main( String[] args ) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                new Program();
            }
        });
    }
}
