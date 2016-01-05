$(function(){
	$.goup({
        trigger: 100,
        bottomOffset: 150,
        locationOffset: 100,
        titleAsText: true,
        //arrowClass:'feedback'
    });
	_slider() ; 
});

function _slider() {
	$('.flexslider').flexslider({
		directionNav: true,
		pauseOnAction: false
	});
}

 