/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.tigris.subversion.svnant.commands;

import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;

import org.tigris.subversion.svnant.SvnAntUtilities;

import java.io.File;

/**
 * svn Copy. Duplicate something in working copy or repos, remembering history.
 * 
 * @author C�dric Chabanois (cchabanois@ifrance.com)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kasisoft.net)
 */
public class Copy extends SvnCommand {

    private static final String MSG_CANT_COPY = "Can't copy";
    private File        srcPath     = null;
    private File        destPath    = null;
    private SVNUrl      srcUrl      = null;
    private SVNUrl      destUrl     = null;
    private boolean     makeparents = false;

    /** revision to copy from (head by default) */
    private SVNRevision revision    = SVNRevision.HEAD;

    /** message for commit (only when target is an url) */
    private String      message     = null;

    /**
     * {@inheritDoc}
     */
    public void execute() {
        try {
            if( srcPath != null ) {
                if( destPath != null ) {
                    getClient().copy( srcPath, destPath );
                } else {
                    getClient().copy( srcPath, destUrl, message );
                }
            } else {
                if( destPath != null ) {
                    getClient().copy( srcUrl, destPath, revision );
                } else {
                    getClient().copy( srcUrl, destUrl, message, revision, makeparents );
                }
            }
        } catch( SVNClientException ex ) {
            throw ex( ex, MSG_CANT_COPY );
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void validateAttributes() {
        SvnAntUtilities.attrsNotSet( "srcPath, srcUrl", true, srcPath, srcUrl );
        SvnAntUtilities.attrsNotSet( "destPath, destUrl", true, destPath, destUrl );
        SvnAntUtilities.attrNotNull( "revision", revision );
        if( destUrl != null ) {
            SvnAntUtilities.attrNotEmpty( "message", message );
        }
    }

    /**
     * set the path to copy from
     * @param srcPath
     */
    public void setSrcPath( File srcPath ) {
        this.srcPath = srcPath;
    }

    /**
     * set the path to copy to
     * @param destPath
     */
    public void setDestPath( File destPath ) {
        this.destPath = destPath;
    }

    /**
     * set the url to copy from
     * @param srcUrl
     */
    public void setSrcUrl( SVNUrl srcUrl ) {
        this.srcUrl = srcUrl;
    }

    /**
     * set the url to copy to
     * @param destUrl
     */
    public void setDestUrl( SVNUrl destUrl ) {
        this.destUrl = destUrl;
    }

    /**
     * set the message for the commit (only when copying directly to repository using an url)
     * 
     * @param message
     */
    public void setMessage( String message ) {
        this.message = message;
    }

    /**
     * Sets the revision
     * 
     * @param revision
     */
    public void setRevision( String revision ) {
        this.revision = getRevisionFrom( revision );
    }

    /**
     * Forces the creation of the parents first if a copy has to be done from a <code>srcUrl</code>
     * to a <code>destUrl</code>.
     * 
     * @param newmakeparents   <code>true</code> <=> Create parents first.
     */
    public void setMakeParents( boolean newmakeparents ) {
        this.makeparents = newmakeparents;
    }

}
