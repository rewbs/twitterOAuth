import oauth.signpost.*
import oauth.signpost.basic.*
import oauth.signpost.http.*
import oauth.signpost.signature.*
import oauth.signpost.exception.*

println '<pre>'
println 'Using the following consumer tokens. They must be the values obtained from dev.twitter.com for your app: '
println 'Consumer key: ' + zget('/config/twitter/consumerKey')
println 'Consumer secrect: ' + zget('/config/twitter/consumerKey')
println 'Change them as appropriate in config/zero.config'
println ''
println ''

if (zget('/request/params/refresh') != null) {
	println 'Clearing out access tokens...'
	println ''
	zdelete('/storage/token')
	zdelete('/storage/secret')
}

if (zget('/storage/token') == null || zget('/storage/secret') == null) {
	println 'This app is not yet authorized to access Twitter.'
	println ''
	authorizeUser()
} else {
	getMentions()
}

/**
 * Walk the user through the authorisation steps
 */
def authorizeUser() {
	println '<strong>Option 1: authorize using a PIN</strong>'
	println 'You will be sent to twitter.com in a new tab, where you will be asked to authorise the app.'
	println 'You will then be asked to enter that pin in a form in this app.'
	println 'After submitting that form, the app will be authorised to access Twitter using your account.'
	println '<a href="usePIN.groovy">Authorize using a PIN!</a>'
	println ''
	println ''
	println '<strong>Option 2: authorize using a callback</strong>'
	println 'Note: for this approach to work, this app must be registered on dev.twitter.com with a callback URI. The domain of the callback URI must match the domain on which this app is running.'
	println 'You will be sent to twitter.com, where you will be asked to authorise the app.'
	println 'You will then be redirected back to the app, at which point it will be authorised to access Twitter using your account.'
	println '<a href="useCallback.groovy">Authorize using a callback!</a>'
}

/**
 * Get mentions for the account which this app is authorized to use.
 */
def getMentions() {
	try {
		//Consumer key & secret from dev.twitter.com are stored in zero.config
		OAuthConsumer consumer = new DefaultOAuthConsumer(
				zget('/config/twitter/consumerKey'),
				zget('/config/twitter/consumerSecret'))
		
		consumer.setTokenWithSecret(zget('/storage/token'), zget('/storage/secret'))
		
		// create a request that requires authentication
		URL url = new URL('http://twitter.com/statuses/mentions.xml')
		HttpURLConnection request = (HttpURLConnection) url.openConnection()
		
		// sign the request
		consumer.sign(request)
		
		// response status should be 200 OK
		println 'Success! This app has been authorized to access twitter using someone\'s twitter account!'
		println 'To prove it, here are the mentions for that account, which can only be accessed when autorised: '
		println '(<a href="?refresh=true">Click here if you want to start afresh and re-auth the app with a different twitter account.</a>)'
		println ''
		println '<hr>'
		println zero.util.XMLEncoder.escapeXML(request.getContent().getText('UTF-8'))
	} catch (Exception e) {
		println 'Oops! Accessing the mentions URL failed with: ' + e
		println 'Try authorizing the app again...'
		println ''
		authorizeUser()
	}	
}