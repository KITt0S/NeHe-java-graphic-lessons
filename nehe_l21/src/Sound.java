import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound implements AutoCloseable {

    private Clip clip = null;
    private AudioInputStream audioInputStream = null;
    private FloatControl volumeControl = null;
    private boolean playing = false; //проигрывается ли звук в данный момент?
    private boolean released = false; //файл загружен?

    Sound( String path ) {

        try {

            File audioFile = new File( path );
            audioInputStream = AudioSystem.getAudioInputStream( audioFile );
            clip = AudioSystem.getClip();
            clip.open( audioInputStream );
            clip.addLineListener( new MyLineListener() );
            volumeControl = ( FloatControl )clip.getControl( FloatControl.Type.MASTER_GAIN );
            released = true;
        } catch ( UnsupportedAudioFileException | IOException | LineUnavailableException e ) {

            e.printStackTrace();
        }
    }

    // Запуск
	/*
	  breakOld определяет поведение, если звук уже играется
	  Если breakOld==true, то звук будет прерван и запущен заново
	  Иначе ничего не произойдёт
	*/
    Sound play( boolean breakOld, boolean isLoop ) {

        if( released ) {

            if( breakOld ) {

                clip.stop();
                clip.setFramePosition( 0 );
                if( isLoop ) {

                    clip.loop( Clip.LOOP_CONTINUOUSLY );
                } else {

                    clip.start();
                }
                playing = true;
            } else if( !playing ) {

                clip.setFramePosition( 0 );
                if( isLoop ) {

                    clip.loop( Clip.LOOP_CONTINUOUSLY );
                } else {

                    clip.start();
                }
                playing = true;
            }
            return this;
        }
        else return null;
    }

    Sound play( boolean isLoop ) {

        return play( true, isLoop );
    }

    Sound play() {

        return play( false );
    }

    void stop() {

        if( playing ) {

            clip.stop();
        }
    }

    @Override
    public void close() throws Exception {

        if( clip != null ) {

            clip.close();
        }
        if( audioInputStream != null ) {

            audioInputStream.close();
        }
    }

    void setVolume( float x ) {

        if( x < 0 ) {

            x = 0;
        }
        if( x > 1 ) {

            x = 1;
        }
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        volumeControl.setValue( ( max - min ) * x + min );
    }

    float getVolume() {

        float value = volumeControl.getValue();
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        return ( value - min ) / ( max - min );
    }

    void join() {

        if( released ) {

            synchronized ( clip ) {

                try {

                    while ( playing ) {

                        clip.wait();
                    }
                } catch ( InterruptedException e ) {

                    e.printStackTrace();
                }
            }
        }
    }

    public static Sound playSound( String path, boolean isLoop ) {

        Sound sound = new Sound( path );
        return sound.play( isLoop );
    }

    public static Sound playSound( String path ) {

        Sound sound = new Sound( path );
        return sound.play();
    }

    private class MyLineListener implements LineListener {


        @Override
        public void update( LineEvent event ) {

            if( event.getType() == LineEvent.Type.STOP ) {

                playing = false;
                synchronized ( clip ) {

                    clip.notify();
                }
            }
        }
    }
}
