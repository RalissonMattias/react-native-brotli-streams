import React, { useEffect } from 'react';

import { StyleSheet, Text, View } from 'react-native';

import fs, { CachesDirectoryPath } from 'react-native-fs';

import { decompressBrotli } from '../../src/index';

export default function App() {
  useEffect(() => {
    const fetchInfos = async () => {
      const url =
        'https://backend.jurishand.com/law/constituicao-de-05-outubro-1988.json.br';

      const baseFileUrl = `${CachesDirectoryPath}/law.json.br`;

      const download = fs.downloadFile({
        fromUrl: url,
        toFile: baseFileUrl,
      });

      if ((await download.promise).statusCode === 200) {
        const content = await fs.readFile(baseFileUrl, 'base64');
        const file = await decompressBrotli(content);
        console.log(file);
      }
    };

    fetchInfos();
  }, []);

  return (
    <View style={styles.container}>
      <Text>Working</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
