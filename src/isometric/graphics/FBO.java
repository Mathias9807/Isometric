package isometric.graphics;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.*;

public class FBO {
	
	private int 	frameBufferID, 
					depthBufferID 	= glGenRenderbuffers(), 
					depthTextureID 	= glGenTextures();
	private int[] 	texturesID;
	public int 		width, 
					height;
	
	public FBO(int w, int h) {
		width = w;
		height = h;
	}

	public FBO withDepthTexture() {
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE8);
		glBindTexture(GL_TEXTURE_2D, depthTextureID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT16, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (java.nio.ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		GL32.glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depthTextureID, 0);
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		return this;
	}

	public FBO withColor(int attachments) {
		frameBufferID = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
		
		texturesID = new int[attachments];
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		for (int i = 0; i < attachments; i++) {
			texturesID[i] = glGenTextures();
			
			glBindTexture(GL_TEXTURE_2D, texturesID[i]);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16F, width, height, 0, GL_RGBA, GL_FLOAT, (java.nio.ByteBuffer) null);
			GL30.glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + i, GL_TEXTURE_2D, texturesID[i], 0);
		}
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		return this;
	}
	
	public FBO withDepth() {
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
		
		glBindRenderbuffer(GL_RENDERBUFFER, depthBufferID);
		glRenderbufferStorage(GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBufferID);
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		return this;
	}
	
	public static void setFrameBuffer(FBO fbo) {
		if (fbo == null) {
			glBindFramebuffer(GL_FRAMEBUFFER, 0);
			glViewport(0, 0, Display.getWidth(), Display.getHeight());
		}else {
			glBindFramebuffer(GL_FRAMEBUFFER, fbo.frameBufferID);
			glViewport(0, 0, fbo.width, fbo.height);
		}
	}
	
	public void bindFrameBuffer() {
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
	}
	
	public int nextPOT(int i) {
		int r = 2;
		while (r < i) 
			r *= 2;
		return r;
	}
	
	public int getFrameBufferId() {
		return frameBufferID;
	}
	
	public int[] getTexturesId() {
		return texturesID;
	}
	
	public int getDepthBufferId() {
		return depthBufferID;
	}
	
	public int getDepthTextureId() {
		return depthTextureID;
	}
	
}
