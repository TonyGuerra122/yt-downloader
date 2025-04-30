pub mod video;

use jni::objects::{JClass, JObject, JString, JValue};
use jni::sys::{jfloat, jobject};
use jni::JNIEnv;
use video::Video;

#[no_mangle]
pub extern "system" fn Java_com_tonyguerra_ytdownloader_utils_YtUtils_getVideo(
    mut env: JNIEnv,
    _class: JClass,
    url: JString,
) -> jobject {
    let url: String = match env.get_string(&url) {
        Ok(s) => s.into(),
        Err(err) => {
            eprintln!("Failed to convert JString to String: {}", err);
            return std::ptr::null_mut();
        }
    };

    let video = Video::from_url(&url);

    let url = env.new_string(&video.url).unwrap();
    let title = env.new_string(&video.title).unwrap();
    let author = match &video.author {
        Some(a) => env.new_string(a).unwrap(),
        None => env.new_string("").unwrap(),
    };
    let duration = video.duration.unwrap_or(0) as f32;

    let description = match &video.description {
        Some(d) => env.new_string(d).unwrap(),
        None => env.new_string("").unwrap(),
    };
    let thumbnail = match &video.thumbnail {
        Some(t) => env.new_string(t).unwrap(),
        None => env.new_string("").unwrap(),
    };

    let class = env
        .find_class("com/tonyguerra/ytdownloader/dto/Video")
        .unwrap();

    let obj = env
        .new_object(
            class,
            "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;FLjava/lang/String;Ljava/lang/String;)V",
            &[
                JValue::Object(&JObject::from(url)),
                JValue::Object(&JObject::from(title)),
                JValue::Object(&JObject::from(author)),
                JValue::Float(jfloat::from(duration)),
                JValue::Object(&JObject::from(description)),
                JValue::Object(&JObject::from(thumbnail)),
            ],
        )
        .unwrap();

    obj.into_raw()
}

#[no_mangle]
pub extern "system" fn Java_com_tonyguerra_ytdownloader_utils_YtUtils_downloadVideo(
    mut env: JNIEnv,
    _class: JClass,
    obj: JObject,
    output_path: JString,
) {
    let output_path: String = env.get_string(&output_path).unwrap().into();

    let video = Video::from_j_object(&mut env, obj);
    video.download(output_path);
}
