import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;

/**
 * Created by KITS on 21.05.2017.
 */
public class BasicFrame implements GLEventListener {
    @Override
    public void init(GLAutoDrawable drawable) {

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }

    public static void main(String[] args) {

        final GLProfile glProfile = GLProfile.get( GLProfile.GL2 );
        GLCapabilities glCapabilities = new GLCapabilities( glProfile );

        GLJPanel gljPanel = new GLJPanel( glCapabilities );
        BasicFrame basicFrame = new BasicFrame();
        gljPanel.addGLEventListener( basicFrame );
        gljPanel.setSize(400, 400 );

        final JFrame jFrame = new JFrame("Basic frame" );
        jFrame.getContentPane().add( gljPanel );
        jFrame.setSize( 400, 400 );
        jFrame.setVisible( true );
    }
}
