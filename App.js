import StaticServer from 'react-native-static-server'
import React, { Component } from 'react'
import RNFS from 'react-native-fs'
import {
  StyleSheet,
  Platform,
  WebView,
  View,
  Text
} from 'react-native'

class ForgeViewer extends Component<{}> {

  constructor (props) {
    super (props)
    this.state = {
      uri: null
    }
  }

  getAssetsPath (platform) {
    switch (platform) {
      case 'ios':
        return RNFS.MainBundlePath + '/assets'
      case 'android':
				
		//var path1 = RNFS.DocumentDirectoryPath + '/test.txt'; 
		//write the file  to data/user/0/com.forgernv/files ===> /data/data/com.forgernv/files
		//RNFS.writeFile(path1, 'Lorem ipsum dolor sit amet', 'utf8') 
		//.then((success) => { console.log('FILE WRITTEN!'); }) 
		//.catch((err) => { console.log(err.message); });
        //return RNFS.DocumentDirectoryPath
        //console.log("javascript get extenal path:"+RNFS.ExternalDirectoryPath)
        //return RNFS.PicturesDirectoryPath // /sdcard/pictures
        return RNFS.ExternalDirectoryPath // /sdcard/android/com.forgernv/files
    }
  }

  componentDidMount () {
    const wwwPath = this.getAssetsPath (Platform.OS)
    const itemName='www'
     this.server = new StaticServer(8080, wwwPath,{
      localOnly : true 
    })
    this.server.start().then((url) => {
      this.setState({
        //uri: `file://${path}/www/test.html`
        //uri: `file://${wwwPath}/${itemName}/index.html`
        //uri: `file://${wwwPath}/${itemName}/index.html`
        //uri: `file:///sdcard/ForgeRNV/www/test.html`
        //uri: `http://www.baidu.com`
        uri:`http://localhost:8080/${itemName}/index.html`
      })
      console.log("Serving at URL:", url)
    }) 
  }

  render() {
    //const scalesPageToFit = (Platform.OS === 'ios') 
    const {uri} = this.state
	console.log('uri is: '+uri)
    return (
	<View style={styles.container}>
      <WebView source={{uri}} style={styles.webView} />
	</View>
    )
  }
}

const styles = StyleSheet.create({
  webView:{
	flex:1
  },
  container: {
        flex: 1,
        paddingTop:20
    }
})

export default class App extends Component<{}> {
  render() {
    return (
	<ForgeViewer /> 
    )
  }
}
