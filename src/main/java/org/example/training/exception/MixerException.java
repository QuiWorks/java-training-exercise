package org.example.training.exception;

public class MixerException extends RuntimeException
{
    public MixerException()
    {
    }

    public MixerException( String message )
    {
        super( message );
    }

    public MixerException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public MixerException( Throwable cause )
    {
        super( cause );
    }

    public MixerException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace )
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
