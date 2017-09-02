public class TC58CommunityUrl {

	private String url;
	
	private String name;
	
	private String id;
	
	private String chusou_num;
	
	private int cur_chusou;
	
	private String chuzu_num;
	
	private int cur_chuzu;
	
	private int part = 0;
	
	private int status = 0;
	
	private String ts;
	
	

	public int getCur_chusou() {
		return cur_chusou;
	}

	public void setCur_chusou(int cur_chusou) {
		this.cur_chusou = cur_chusou;
	}

	public int getCur_chuzu() {
		return cur_chuzu;
	}

	public void setCur_chuzu(int cur_chuzu) {
		this.cur_chuzu = cur_chuzu;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChusou_num() {
		return chusou_num;
	}

	public void setChusou_num(String chusou_num) {
		this.chusou_num = chusou_num;
	}

	public String getChuzu_num() {
		return chuzu_num;
	}

	public void setChuzu_num(String chuzu_num) {
		this.chuzu_num = chuzu_num;
	}

	public int getPart() {
		return part;
	}

	public void setPart(int part) {
		this.part = part;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	
	@Override
	public String toString() {
		return "TC58CommunityUrl [url=" + url + ", name=" + name + ", id=" + id + ", chusou_num=" + chusou_num
				+ ", cur_chusou=" + cur_chusou + ", chuzu_num=" + chuzu_num + ", cur_chuzu=" + cur_chuzu + ", part="
				+ part + ", status=" + status + ", ts=" + ts + "]";
	}

}
