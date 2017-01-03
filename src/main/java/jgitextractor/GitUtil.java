package jgitextractor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;

public class GitUtil {
	
	private Repository repo;
	private Git git;
	
	public GitUtil() throws InvalidRemoteException, TransportException, IllegalStateException, GitAPIException{
		 git = Git.cloneRepository()
				.setURI("https://github.com/Irvayne/AuroraBoreal")
				.setBare(false)
				.setBranch("master")//define qual ramo sera extraido. Funciona tanto para branch como para tags
				//.setCredentialsProvider(new UsernamePasswordCredentialsProvider("irvaynematheus@yahoo.com", "ronaldo920021995"))
				.call();

		repo = git.getRepository();
	}
	
	public void getCommits() throws NoHeadException, GitAPIException, AmbiguousObjectException, IncorrectObjectTypeException, IOException{
		 
			Iterable<RevCommit> log = git.log().setMaxCount(500).call();
			
			for (RevCommit jgitCommit: log) {

				System.out.println("Id do commit = "+jgitCommit.getName());
				System.out.println("Author do commit = "+jgitCommit.getAuthorIdent().getName());
				List<DiffEntry> diffsForTheCommit = diffsForTheCommit(repo, jgitCommit);
				for (DiffEntry diff : diffsForTheCommit) {
					System.out.print("Tipo de alteracao no arquivo = "+diff.getChangeType().toString());

					System.out.println(" - Nome do arquivo = "+diff.getNewPath());

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					DiffFormatter diffFormatter = new DiffFormatter( stream );
					diffFormatter.setRepository(repo);
					diffFormatter.format(diff);
					String in = stream.toString();
					DiffUtil util = new DiffUtil();
					Map<String, Integer> response = util.analyze(in);
					System.out.println("Adicao = "+response.get("adds"));
					System.out.println("Modificacao = "+response.get("mods"));
					System.out.println("Delecao = "+response.get("dels"));
					System.out.println("Condicao = "+response.get("conditions"));
					System.out.println();
					diffFormatter.flush();
				}
				System.out.println();


			}
			
			getDirTree();
	}

	private Set<DirTree> getDirTree() throws IOException{
		Ref head = repo.getRef("HEAD");
		
		RevWalk walk = new RevWalk(repo);
		RevCommit commit = walk.parseCommit(head.getObjectId()); 
        RevTree tree = commit.getTree(); 
        System.out.println("Having tree: " + tree);
				
		 
	     TreeWalk treeWalk = new TreeWalk(repo);
	     treeWalk.addTree(tree); 
        // treeWalk.setRecursive(true); 
        	// treeWalk.setFilter(PathFilter.create(path));
         
         
         Set<DirTree> dirTree = new HashSet<DirTree>();
 		
 		while(treeWalk.next()){
 			
 			DirTree aux = new DirTree();
 			aux.setText(treeWalk.getPathString());
 			
 		if(!treeWalk.isSubtree()){
 			System.out.println(treeWalk.getPathString());
 			dirTree.add(aux);
 			continue;
 		}else{
 			System.out.println(treeWalk.getPathString());
 			treeWalk.enterSubtree();
 			
 			}
 		}	
         
        return dirTree;

	}
	
	private void imprime(String pai, Set<DirTree> dirTree){
		for(DirTree tree: dirTree){
			System.out.println(pai+" ---- "+tree.getText());
			if(!tree.getChildren().isEmpty())
				imprime(tree.getText(), tree.getChildren());
			
				
		}
	}
	
	

	private  List<DiffEntry> diffsForTheCommit(Repository repo, RevCommit commit) throws IOException, AmbiguousObjectException, 
	IncorrectObjectTypeException { 

		AnyObjectId currentCommit = repo.resolve(commit.getName()); 
		AnyObjectId parentCommit = commit.getParentCount() > 0 ? repo.resolve(commit.getParent(0).getName()) : null; 

		DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE); 
		df.setBinaryFileThreshold(2 * 1024); // 2 mb max a file 
		df.setRepository(repo); 
		df.setDiffComparator(RawTextComparator.DEFAULT); 
		df.setDetectRenames(true); 
		List<DiffEntry> diffs = null; 

		if (parentCommit == null) { 
			RevWalk rw = new RevWalk(repo); 
			diffs = df.scan(new EmptyTreeIterator(), new CanonicalTreeParser(null, rw.getObjectReader(), commit.getTree())); 
			rw.close(); 
		} else { 
			diffs = df.scan(parentCommit, currentCommit); 
		} 

		df.close(); 

		return diffs; 
	}
}
