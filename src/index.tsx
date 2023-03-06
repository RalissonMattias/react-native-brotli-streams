import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-brotli-streams' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const BrotliStreams = NativeModules.BrotliStreams
  ? NativeModules.BrotliStreams
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function decompressBrotli(file: string): Promise<number> {
  return BrotliStreams.decompressBrotli(file);
}

export function base64ToString(file: string): Promise<number> {
  return BrotliStreams.decompressBrotli(file);
}
