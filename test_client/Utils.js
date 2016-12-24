// 数组的删除
Array.prototype.remove=function(dx)
　{
	　　if(isNaN(dx)||dx>this.length){return false;}
	　　for(var i=0,n=0;i<this.length;i++)
	　　{
		　　　　if(this[i]!=this[dx])
		　　　　{
			　　　　　　this[n++]=this[i]
		　　　　}
	　　}
	　　this.length-=1
　}


// 将mousedown的坐标转换成在 canvas 上的坐标
function getPointOnCanvas(x,y){
	var bbox = canvas.getBoundingClientRect();
	return { x: x - bbox.left * (canvas.width  / bbox.width),  
		y: y - bbox.top  * (canvas.height / bbox.height)  
	}; 
}