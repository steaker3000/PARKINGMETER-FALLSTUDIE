package ch.zhaw.pm.plugin.components;

public enum PluginComponentType {
	VIEW("View"), INPUT("Input"), DUMMY("Dummy");

	private String typeName;

	PluginComponentType (String typeName) {
		this.typeName = typeName;
	}
	
	public String getTypeName(){
		return this.typeName;
	}
}



