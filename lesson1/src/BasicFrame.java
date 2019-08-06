import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import java.awt.*;

/**
 * Created by KITS on 21.05.2017.
 */
public class BasicFrame implements GLEventListener {

    @Override
    public void init( GLAutoDrawable drawable ) {


    }

    @Override
    public void dispose( GLAutoDrawable drawable ) {


    }

    @Override
    public void display( GLAutoDrawable drawable ) {


    }

    @Override
    public void reshape( GLAutoDrawable drawable, int x, int y, int width, int height ) {


    }

    public static void main( String[] args ) {

        final GLProfile profile = GLProfile.get( GLProfile.GL2 );
        GLCapabilities capabilities = new GLCapabilities( profile );
        final GLCanvas glCanvas = new GLCanvas( capabilities );

        BasicFrame basicFrame = new BasicFrame();
        glCanvas.addGLEventListener( basicFrame );
        glCanvas.setSize( 400, 400 );

        final Frame frame = new Frame( "Basic frame" );
        frame.add( glCanvas );
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize( screenSize.width, screenSize.height );
        frame.setVisible( true );
    }
}
