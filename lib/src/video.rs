use std::{path::Path, process::Command};

use jni::{
    objects::{JObject, JString},
    JNIEnv,
};

pub struct Video {
    pub url: String,
    pub title: String,
    pub author: Option<String>,
    pub duration: Option<u32>,
    pub description: Option<String>,
    pub thumbnail: Option<String>,
}

impl Video {
    pub fn new(
        url: &str,
        title: String,
        author: Option<String>,
        duration: Option<u32>,
        description: Option<String>,
        thumbnail: Option<String>,
    ) -> Self {
        Video {
            url: url.to_string(),
            title,
            author,
            duration,
            description,
            thumbnail,
        }
    }

    pub fn download(&self, output_dir: String) {
        let sanitized_title = self
            .title
            .replace(|c: char| !c.is_alphanumeric() && c != ' ', "_");

        // Garante que o diretório termine com `/`
        let dir = Path::new(&output_dir);
        let full_path = dir.join(format!("{}.%(ext)s", sanitized_title));

        let output = Command::new("yt-dlp")
            .arg(&self.url)
            .arg("--output")
            .arg(full_path.to_string_lossy().to_string())
            .output()
            .expect("Failed to execute yt-dlp");

        if output.status.success() {
            println!("Downloaded: {}", self.title);
        } else {
            eprintln!("Failed to download: {}", self.title);
            let error_message = String::from_utf8_lossy(&output.stderr);
            eprintln!("Error: {}", error_message);
        }
    }

    fn parse_json(json_raw: String) -> Self {
        let json: serde_json::Value = match serde_json::from_str(&json_raw) {
            Ok(data) => data,
            Err(err) => {
                eprintln!("Failed to parse JSON: {}", err);
                return Video::new("", String::new(), None, None, None, None);
            }
        };

        let title = json["title"].as_str().unwrap_or("").to_string();
        let author = json["author"].as_str().map(|s| s.to_string());
        let duration = json["duration"].as_u64().map(|d| d as u32);
        let description = json["description"].as_str().map(|s| s.to_string());
        let thumbnail = json["thumbnail"].as_str().map(|s| s.to_string());

        Video::new("", title, author, duration, description, thumbnail)
    }

    pub fn from_url(url: &str) -> Self {
        let output = Command::new("yt-dlp")
            .arg("--dump-json")
            .arg(url)
            .output()
            .expect("Failed to execute yt-dlp");

        if output.status.success() {
            let json_raw = String::from_utf8_lossy(&output.stdout).to_string();
            let mut video = Self::parse_json(json_raw);

            video.url = url.to_string();

            video
        } else {
            eprintln!("yt-dlp failed to execute");
            std::process::exit(1);
        }
    }

    pub fn from_j_object(env: &mut JNIEnv, obj: JObject) -> Self {
        fn get_string_field(env: &mut JNIEnv, obj: &JObject, name: &str) -> Option<String> {
            env.get_field(obj, name, "Ljava/lang/String;")
                .ok()
                .and_then(|v| v.l().ok())
                .and_then(|jobj| {
                    let jstr: JString = JString::from(jobj);
                    env.get_string(&jstr).ok().map(|s| s.into())
                })
        }

        let url = get_string_field(env, &obj, "url").unwrap_or_default();
        let title = get_string_field(env, &obj, "title").unwrap_or_default();
        let author = get_string_field(env, &obj, "author");
        let description = get_string_field(env, &obj, "description");
        let thumbnail = get_string_field(env, &obj, "thumbnail");

        let duration = env
            .get_field(obj, "duration", "F")
            .ok()
            .and_then(|v| v.f().ok())
            .map(|f| f as u32);

        Video {
            url,
            title,
            author,
            duration,
            description,
            thumbnail,
        }
    }
}
