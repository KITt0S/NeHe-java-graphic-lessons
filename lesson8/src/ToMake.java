import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by KITS on 22.05.2017.
 */
public class ToMake {

    public static void main(String[] args) {

        final GLProfile profile = GLProfile.get( GLProfile.GL2 );
        GLCapabilities capabilities = new GLCapabilities( profile );

        final GLCanvas canvas = new GLCanvas( capabilities );
        Cube cube = new Cube();
        canvas.addGLEventListener( cube );
        canvas.addKeyListener( cube );
        canvas.setSize( 400, 400 );

        final JFrame frame = new JFrame("Вращающийся куб с текстурами");
        frame.getContentPane().add( canvas );
        frame.setSize( frame.getContentPane().getPreferredSize() );
        frame.setVisible( true );


        final FPSAnimator animator = new FPSAnimator( canvas, 300, true );
        animator.start();
    }
}
