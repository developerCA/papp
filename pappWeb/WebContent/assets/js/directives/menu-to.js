'use strict';
/** 
  * Menu-check directive.
*/
app.directive('navigation', ['$compile' ,'$sce','$animate', '_' , function($compile,$sce,$animate, _) {
    return {
      restrict: 'E',
      scope: {
        menu: "=menu"
      },
      link: function(scope,elem,attrs){
    	  
    	  var menuHtml='';
    	 
    	  scope.$watch('menu', function(newValue, oldValue) {
    		  
    		  if (newValue)
            	 crearMenu();
              
          }, true);

          function crearMenu(){
        	  var menusPadre = _.filter(scope.menu, function(menu){ return menu.padreid==0 && menu.nombre.trim()!="" });
        	
        	  menuHtml=' <nav> <ul class="main-navigation-menu">';
        	  
    		  if (seccion.cambiarclave == '1') {
        		  menuHtml+='<li ng-class="{\'active open\':$state.includes(\''+ '' +'\')}">';
         		  //menuHtml+='<li class="">';
        		  menuHtml+=' <a ui-sref="app.cambiarcontrasena">';
        		  menuHtml+='	<div class="item-content">';
        		  menuHtml+='    <div class="item-media">';
        		  menuHtml+='    <span><i class=" ti-line-double"></i></span>';
        		  menuHtml+='    </div>';
        		  menuHtml+='	 <div class="item-inner">';
        		  menuHtml+='	  <span class="title" > Cambiar clave </span><i class="icon-arrow"></i>';
        		  menuHtml+='    </div>';
        		  menuHtml+='   </div>';
        		  menuHtml+=' </a>';
        		  menuHtml+='</li>';
    		  } else {
	        	  angular.forEach(menusPadre, function(men) {
	        		  if (!ifRollId(men.permisoid)) {
	        			  console.log("[RAIZ] NO MOSTRADO para:", men);
	        			  return;
	        		  }
	        		  menuHtml+='<li ng-class="{\'active open\':$state.includes(\''+ '' +'\')}">';
	         		  //menuHtml+='<li class="">';
	        		  menuHtml+=' <a >';
	        		  menuHtml+='	<div class="item-content">';
	        		  menuHtml+='    <div class="item-media">';
	        		  menuHtml+='    <span><i class=" ti-line-double"></i></span>';
	        		  menuHtml+='    </div>';
	        		  menuHtml+='	 <div class="item-inner">';
	        		  menuHtml+='	  <span class="title" > '+ men.nombre+' </span><i class="icon-arrow"></i>';
	        		  menuHtml+='    </div>';
	        		  menuHtml+='   </div>';
	        		  menuHtml+=' </a>';
	        		 
	        		  var menusHijos = _.filter(scope.menu, function(menu){ return menu.padreid==men.id && menu.nombre.trim()!="" });
	              	
	        		  if (menusHijos.length>0){
	        			  menuHtml+=crearMenuHijos(menusHijos);
	        		  }
	        			
	        		  menuHtml+='</li>';
	        	  });
    		  }
        	  
        	  menuHtml+='</ul> </nav>';

        	  if (menuHtml!=""){
            	  var content = $compile(menuHtml)(scope);
            	  elem.html(content);
        	  }
          };

    	  function crearMenuHijos(hijos){
    	   var menuHtml="";
    	   
    	   menuHtml+='<ul class="sub-menu">';
    	   angular.forEach(hijos, function(men) {
     		  if (!ifRollId(men.permisoid)) {
    			  console.log("NO MOSTRADO para:", men);
    			  return;
    		  }
    		   var menusHijos = _.filter(scope.menu, function(menu){ return menu.padreid==men.id && menu.nombre.trim()!="" });

    		   if (menusHijos.length>0){
    			   menuHtml+='<li>';  
    			   menuHtml+='<a>';
    			   menuHtml+='<span>'+men.nombre+'</span><i class="icon-arrow"></i> </a>';
    		   }else{
    			 menuHtml+='<li ui-sref-active="active">';
    			 if (men.ruta==""){
    				 menuHtml+='<a><span>'+men.nombre+'</span></a>';
    			 }else{
    				 menuHtml+='<a ui-sref="'+men.ruta+'"><span>'+men.nombre+'</span></a>';
    			 }
    			 
      			 //menuHtml+='<a ui-sref="app.pagelayouts.fixedheader"><span>'+men.nombre+'</span></a>';
      		   }

    		   if (menusHijos.length>0){
     			  menuHtml+=  crearMenuHijos(menusHijos);
     		   }
		      
    		   menuHtml+='</li>';
    	   });
    	   
    	   menuHtml+='</ul>';
		    
    		return menuHtml;
          
    	  };
      },
      
      
    }
  }]);
