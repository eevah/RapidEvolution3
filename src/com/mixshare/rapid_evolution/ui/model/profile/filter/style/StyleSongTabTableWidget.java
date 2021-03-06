package com.mixshare.rapid_evolution.ui.model.profile.filter.style;

import org.apache.log4j.Logger;

import com.mixshare.rapid_evolution.data.search.parameters.search.SearchSearchParameters;
import com.mixshare.rapid_evolution.ui.UIProperties;
import com.mixshare.rapid_evolution.ui.model.column.Column;
import com.mixshare.rapid_evolution.ui.model.profile.filter.FilterProfileSongsModel;
import com.mixshare.rapid_evolution.ui.model.profile.tab.RecordTabTableProxyModel;
import com.mixshare.rapid_evolution.ui.model.table.RecordTableModelManager;
import com.mixshare.rapid_evolution.ui.model.table.TableModelManager;
import com.mixshare.rapid_evolution.ui.util.Translations;
import com.mixshare.rapid_evolution.ui.widgets.common.table.SortTableView;
import com.mixshare.rapid_evolution.ui.widgets.profile.filter.search_tabs.FilterSongTabTableView;
import com.mixshare.rapid_evolution.ui.widgets.profile.tab.RecordTabTableView;
import com.mixshare.rapid_evolution.ui.widgets.profile.tab.RecordTabTableWidget;

public class StyleSongTabTableWidget extends RecordTabTableWidget {

    static private Logger log = Logger.getLogger(StyleSongTabTableWidget.class);	
	
    private FilterProfileSongsModel model;	
	private StyleSongProxyModel proxyModel;
	
	public StyleSongTabTableWidget(RecordTableModelManager modelManager, Column defaultSortColumn) {
		super(modelManager, defaultSortColumn);
		this.model = (FilterProfileSongsModel)modelManager.getModelPopulator();		
	}
	
	public RecordTabTableProxyModel getTabTableProxyModel(RecordTabTableWidget tabTableWidget, RecordTableModelManager tableManager, RecordTabTableView tabTableView) {
		proxyModel = new StyleSongProxyModel(tabTableWidget, tableManager, tabTableView, this);
		if (UIProperties.hasProperty("style_songs_show_type"))
			proxyModel.setFilterType(UIProperties.getByte("style_songs_show_type"));		
		return proxyModel;
	}
	
    public String[] getShowTypes() { return new String[] { Translations.get("songs_all"), Translations.get("songs_mine"), Translations.get("songs_new") }; }
	public byte getShowType() {
		if (UIProperties.hasProperty("style_songs_show_type"))
			return UIProperties.getByte("style_songs_show_type");
		return 0;
	}

    protected SortTableView createTableView(TableModelManager modelManager) {
    	return new FilterSongTabTableView((RecordTableModelManager)modelManager);    	
    }
	
	public void setShowType() { 
    	if (proxyModel != null) {  		
    		if (searchBarWidget.getFilterCombo().currentIndex() == 0) {
    			proxyModel.setFilterType(StyleSongProxyModel.FILTER_TYPE_ALL_SONGS);
    			((SearchSearchParameters)proxyModel.getSearchParameters()).setInternalItemsOnly(false);
    			((SearchSearchParameters)proxyModel.getSearchParameters()).setExternalItemsOnly(false);
    		} else if (searchBarWidget.getFilterCombo().currentIndex() == 1) {
    			proxyModel.setFilterType(StyleSongProxyModel.FILTER_TYPE_MY_SONGS);
    			((SearchSearchParameters)proxyModel.getSearchParameters()).setInternalItemsOnly(true);
    			((SearchSearchParameters)proxyModel.getSearchParameters()).setExternalItemsOnly(false);
    		} else if (searchBarWidget.getFilterCombo().currentIndex() == 2) {
    			proxyModel.setFilterType(StyleSongProxyModel.FILTER_TYPE_NEW_SONGS);
    			((SearchSearchParameters)proxyModel.getSearchParameters()).setInternalItemsOnly(false);
    			((SearchSearchParameters)proxyModel.getSearchParameters()).setExternalItemsOnly(true);
    		}
    		UIProperties.setProperty("style_songs_show_type", String.valueOf(searchBarWidget.getFilterCombo().currentIndex()));
    		updateFilter();
    	}    	
    }
	
	public StyleSongProxyModel getProxyModel() {
		return proxyModel;
	}

    public void updateFilter() {
    	if (log.isTraceEnabled())
    		log.trace("updateFilter(): invalidating...");
    	if (searchBarWidget != null) {
	    	((SearchSearchParameters)proxyModel.getSearchParameters()).setSearchText(searchBarWidget.getFilterText().text());
	    	if (model.update()) {
	    		modelManager.refresh();
	    		proxyModel.invalidate();
	    	}
    	}
    }	
}
