package com.velisphere.tigerspice.client.helper.widgets;

import java.util.ArrayList;
import java.util.List;

import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.user.client.TakesValue;

public class EnhancedListBox extends ListBox implements TakesValue<String>

{
	private List<String> listItems = new ArrayList<String>();
	private String value;

	public void setValue(String value) {
		if (listItems.size() > 0) {
			int valueIndex = 0;
			if (listItems.contains(value)) {
				valueIndex = listItems.indexOf(value);
				this.value = value;
			}
			setItemSelected(valueIndex, true);
		}
	}

	public String getValue() {
		int selectedIndex = super.getSelectedIndex();
		String value = null;
		if (selectedIndex >= 0) {
			value = super.getValue(selectedIndex);
			if ("null".equals(value)) {
				value = null;
			}
		}

		return value;
	}

	public void setOptions(List<String> items) {
		listItems.clear();
		listItems.addAll(items);
		for (String item : listItems) {
			addItem(item, item);
		}
	}
}