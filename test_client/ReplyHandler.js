

function process(obj){
	if (obj.op == "alldata") {
		var arr = obj.userstate;
		for(var i = 0; i < arr.length; ++i){
			recvMsg.push(arr[i]);
		}
	}
}