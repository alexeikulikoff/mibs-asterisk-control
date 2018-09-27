QUnit.test( "hello test", function( assert ) {
		 console.log("hello tt");
		  assert.ok( 1 == "1", "Passed!" );
});

QUnit.test("'Return XMLHttpRequest object from $.ajax'", function(assert){
	
	
	$.mockjax({
		url: '/xmlhttprequest',
		responseText: 'Hello Word'
	});

	var xhr = $.ajax({
		url: '/xmlhttprequest',
		complete: function() { }
	});
	if (xhr && xhr.abort) {
		xhr.abort();
	}

	assert.ok(xhr, 'XHR object is not null or undefined');
	assert.ok(xhr.done && xhr.fail, 'Got Promise methods');

});

QUnit.('Support 1.5 $.ajax(url, settings) signature.', function(assert) {
	var done = assert.async();

	$.mockjax({
		url: '/resource',
		responseText: 'Hello World'
	});

	$.ajax('/resource', {
		success: function(response) {
			assert.equal(response, 'Hello World');
		},
		error: qunit.noErrorCallbackExpected,
		complete: done
	});
});
t('Response returns json', function(assert) {
	var done = assert.async();
	
	$.mockjax({
		url: '/json',
		contentType: 'text/json',
		responseText: { 'foo' : 'bar', 'baz' : { 'car' : 'far' } }
	});
	$.ajax({
		url: '/json',
		dataType: 'json',
		success: function(json) {
			assert.deepEqual(json, { 'foo' : 'bar', 'baz' : { 'car' : 'far' } }, 'JSON Object matches');
		},
		error: qunit.noErrorCallbackExpected,
		complete: function(xhr) {
			assert.equal(xhr.getResponseHeader('Content-Type'), 'text/json', 'Content type of text/json');
			done();
		}
	});
});

