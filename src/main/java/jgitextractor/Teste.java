package jgitextractor;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;

public class Teste {

	public static void main(String[] args) throws InvalidRemoteException, TransportException, IllegalStateException, GitAPIException, AmbiguousObjectException, IncorrectObjectTypeException, IOException {
		
		GitUtil util = new GitUtil();
		util.getCommits();
		
		

	}
	
	

}
