FccTVApp.utils.AppUtils = {
	/**
	 * date list renderer
	 * @param tpl template
	 * @param date Date object
	 * @returns rendered string
	 */
	renderDailyTpl:	function(tpl, date){
		//  year: {year}, month: {month}, date: {date}, day of week: {dayofweek}
		var dayofweek = [bundle.getText('dailylist.item.week.sun'), 
		                 bundle.getText('dailylist.item.week.mon'),
		                 bundle.getText('dailylist.item.week.tue'),
		                 bundle.getText('dailylist.item.week.wed'),
		                 bundle.getText('dailylist.item.week.thu'),
		                 bundle.getText('dailylist.item.week.fri'),
		                 bundle.getText('dailylist.item.week.sat')]
		return tpl.replace(/{year}/g, date.getFullYear()).replace(/{month}/g, (date.getMonth() + 1)).replace(/{date}/g, date.getDate()).replace(/{dayofweek}/g, dayofweek[date.getDay()]);
	}
}

