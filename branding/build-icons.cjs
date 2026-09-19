// Deterministic Android export of the supplied logo; no generated artwork.
const sharp = require(process.env.SHARP_MODULE || 'sharp');
const path = require('path');
const root = path.resolve(__dirname, '..');
const source = path.join(__dirname, 'ev-logo.png');
const res = path.join(root, 'lemuroid-app/src/main/res');
(async () => {
  for (const [density, size] of Object.entries({ mdpi: 48, hdpi: 72, xhdpi: 96, xxhdpi: 144, xxxhdpi: 192 })) {
    for (const name of ['lemuroid_launcher', 'lemuroid_launcher_round']) {
      await sharp(source).resize(size, size).png().toFile(path.join(res, `mipmap-${density}`, `${name}.png`));
    }
  }
  const foreground = await sharp(source).resize(288, 288).png().toBuffer();
  await sharp({ create: { width: 432, height: 432, channels: 4, background: '#000000' } })
    .composite([{ input: foreground, gravity: 'centre' }]).png()
    .toFile(path.join(res, 'drawable/ev_logo_foreground.png'));
  await sharp(source).resize(320, 180, { fit: 'contain', background: '#000000' }).png()
    .toFile(path.join(res, 'mipmap-xhdpi/ic_banner.png'));
  await sharp(source).resize(512, 512).png().toFile(path.join(root, 'lemuroid-app/src/main/lemuroid_launcher-web.png'));
})();
