import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;

public class Solution extends JFrame {

    Solution() {

        GLProfile profile = GLProfile.get( GLProfile.GL2 );
        GLCapabilities capabilities = new GLCapabilities( profile );
        GLCanvas canvas = new GLCanvas( capabilities );
        canvas.setSize( new Dimension( 400, 400 ) );
        Field field = new Field();
        canvas.addKeyListener( field );
        canvas.addGLEventListener( field );
        canvas.setFocusable( true );
        getContentPane().add( canvas );
        setSize( getContentPane().getPreferredSize() );
        setVisible( true );
        setTitle( "Сверкающие звезды" );
        FPSAnimator animator = new FPSAnimator( canvas, 300, true );
        animator.start();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {

                new Solution();
            }
        });
    }
}
