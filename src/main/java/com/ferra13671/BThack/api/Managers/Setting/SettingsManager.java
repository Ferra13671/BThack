package com.ferra13671.BThack.api.Managers.Setting;

import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;

import java.util.ArrayList;
import java.util.Objects;

public class SettingsManager {

	//final added
	private final ArrayList<Setting> ModuleSettings;
	
	public SettingsManager(){
		this.ModuleSettings = new ArrayList<>();
	}
	
	public void addModuleSetting(Setting in){
		this.ModuleSettings.add(in);
	}
	
	public ArrayList<Setting> getModuleSettings(){
		return this.ModuleSettings;
	}
	
	public ArrayList<Setting> getSettingsByMod(Module mod){
		ArrayList<Setting> out = new ArrayList<>();
		for(Setting s : getModuleSettings()) {
			if (s != null) {
				if (s.getModule().equals(mod)) {
					out.add(s);
				}
			}
		}
		if(out.isEmpty()) {
			return null;
		}
		return out;
	}
	
	public Setting getModuleSettingByName(String mod, String name){
		for(Setting set : getModuleSettings()){
			if(set.getName().equalsIgnoreCase(name) && Objects.equals(set.getModule().name, mod)){
				return set;
			}
		}
		return null;
	}
}
