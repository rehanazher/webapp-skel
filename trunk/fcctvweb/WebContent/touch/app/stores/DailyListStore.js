var dayList = (function() {
	var resultData = [];
	
	var date = new Date();
	var dateOfMonth = date.getDate();
	
	for (var i = 0; i < 18; i++){
		var groupTpl = bundle.getText('dailylist.group.format');
		var itemTpl = bundle.getText('dailylist.item.format');
		resultData[i] = {
				group: FccTVApp.utils.AppUtils.renderDailyTpl(groupTpl, date), 
				value: FccTVApp.utils.AppUtils.renderDailyTpl(itemTpl, date), 
				date: date.getFullYear() + "-" + (date.getMonth() < 9 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1)) + "-" + (date.getDate() < 10) ? "0" + date.getDate() : date.getDate(),
				
				};
		date.setDate(dateOfMonth-1);
		dateOfMonth = date.getDate();
	}
	return resultData;
})();


Ext.regModel('DailyListModel', {
    fields: ['group', 'value', 'date']
});

FccTVApp.stores.DailyListStore = new Ext.data.JsonStore({
    model  : 'DailyListModel',
    
    getGroupString : function(record) {
        return record.get('group');
    },

    data: dayList
});
