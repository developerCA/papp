'use strict';
/** 
  * Menu-check directive.
*/
app.directive('navigation', ['$compile' ,'$sce','$animate' , function($compile,$sce,$animate) {
    return {
      restrict: 'EA',
      scope: {
        menuDym: '=menu'
      },
      replace:true,
      link: function(scope,elem,attrs){
    	  
    	  var menuHtml='';
    	  
    	  scope.$watch('menuDym', function(newValue, oldValue) {
              if (newValue)
            	 crearMenu();
              
          }, true);
    	  
    	  
    	  
    	           
          function crearMenu(){
        	  
        	  menuHtml=' <nav> <ul class="main-navigation-menu">';

        	  
        	  menuHtml+='<li ui-sref-active="active"><a ui-sref="app.dashboard">';
        	  menuHtml+='	<div class="item-content">';
        	  menuHtml+='		<div class="item-media">';
        	  menuHtml+='			<i class="ti-home"></i>';
        	  menuHtml+='		</div>';
        	  menuHtml+='		<div class="item-inner">';
        	  menuHtml+='			<span class="title" >';
        	  menuHtml+='				Home </span>';
        	  menuHtml+='		</div>';
        	  menuHtml+='	</div>';
        	  menuHtml+='</a></li>';
    	
        	  angular.forEach(scope.menuDym, function(men) {
        		
        		  menuHtml+='<li ng-class="{\'active open\':$state.includes(\''+ men.ruta +'\')}">';
        		  menuHtml+='<a href="javascript:void(0)"  > ';
        		  menuHtml+='<div class="item-content"> ';
        		  menuHtml+='<div class="item-media">' ;
        		  menuHtml+='<i class="'+men.icono+'"></i>';
        		  menuHtml+='</div>';
        		  menuHtml+='<div class="item-inner">';
        		  menuHtml+='<span class="title" >'+men.nombre+'<i class="icon-arrow"></i></span>' ;
        		  menuHtml+='</div>' ;
        		  menuHtml+='</div>';
        		  menuHtml+='</a>' +crearMenuHijos(men.permisos);
        		  menuHtml+='</li>';
        	
        	  });

        	  menuHtml+='</ul> </nav>';
        	  
        	  //console.log(menuHtml);
        	
        	  var content = $compile(menuHtml)(scope);
        	
        	  elem.html(content);
  
          };
         
          
    	  function crearMenuHijos(hijos){
    	   var menuHtml="";
    	   
    		  if (hijos.length>0){
    			  menuHtml+='<ul class="sub-menu">';
    			  angular.forEach(hijos, function(men) {
    	    		  
    	    		  menuHtml+='<li ui-sref-active="active">  <a ui-sref="'+men.url+'"> <div class="item-content"> <i class="'+men.icono+'"></i> <span class="title">'+'  '+men.nombre + '</span></div> </a></li>' ;
    	    	  });
    			  menuHtml+="</ul>";
    		  }
    		  
    		  return menuHtml;
          };
      },
      
      
    }
  }]);
