/**
 * 
 */
package jgitextractor;

import java.util.HashSet;
import java.util.Set;





/**
 * @author Werney Ayala
 *
 */

public class DirTree{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Long id;
	
	private String text;
	
	
	private String type;
	
	
	private Set<DirTree> children;
	
	public DirTree(){
		this.children = new HashSet<DirTree>();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the nodes
	 */
	public Set<DirTree> getChildren() {
		return children;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setChildren(Set<DirTree> nodes) {
		this.children = nodes;
	}
	
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public boolean equals(Object other){
		DirTree dirTree = (DirTree) other;
		return (this.text.equals(dirTree.getText()) ? true : false);
	}

	
	
}
